import { styled } from 'styled-components';
import { ActionBar } from '@components/ActionBar/ActionBar';
import { PostBar } from '@components/ActionBar/PostBar';
import { TextButton } from '@components/Button/TextButton';
import { Header } from '@components/Header/Header';
import { Icon } from '@components/Icon/Icon';
import { SellerInfo } from '@components/SellerInfo/SellerInfo';
import { States } from '@components/States/States';
import { usePageNavigator } from '@hooks/usePageNavigator';
import displayTimeAgo from '@utils/displayTimeAgo';
import { useAtom } from 'jotai';
import { userInfoAtom } from '@atoms/userAtom';
import { MenuDropdown } from '@components/Dropdown/MenuDropdown';
import { STATES_OF_PRODUCT } from '@constants/constants';
import { useModal } from '@components/Modal/useModal';
import {
  useChangeProductStatusMutation,
  useDeleteProductMutation,
} from '@api/product/product';
import { Product, ProductStatProps, ProductStatus } from '@api/product/types';
import { QUERY_KEYS } from '@api/queryKey';
import { useToast } from '@components/Toast/useToast';
import { usePostChatRoom } from '@api/chat/chat';

export const DetailPage = ({
  productData,
  goEditPage,
  productStat,
}: {
  productData: Product;
  productStat: ProductStatProps;
  goEditPage(): void;
}) => {
  const [userInfo] = useAtom(userInfoAtom);
  const toast = useToast();
  const postChatRoom = usePostChatRoom();
  const { navigateToGoBack, navigateToHome } = usePageNavigator();
  const { openModal } = useModal();
  const changeProductStatus = useChangeProductStatusMutation(
    QUERY_KEYS.PRODUCT_DETAIL(productData.id)
  );
  const deleteProduct = useDeleteProductMutation(
    QUERY_KEYS.PRODUCT_DETAIL(productData.id)
  );

  const handleChatButton = () => {
    const id = { productId: productData.id, sellerId: productData.writer.id };
    postChatRoom.mutate(id);
  };

  const openConfirmAlert = (productName: string) => {
    openModal('alert', {
      message: `${productName}을(를) 삭제하시겠습니까?`,
      leftButtonText: '취소',
      rightButtonText: '삭제',
      onDelete: () => {
        deleteProduct.mutate(productData.id);
        toast.noti(`${productName}(이)가 삭제되었습니다`);
        navigateToHome();
      },
    });
  };

  const isWriter = userInfo?.id === productData.writer.id;

  return (
    <>
      <Header backgroundColor="none">
        <Header.Left>
          <TextButton
            size="M"
            textColor="accentText"
            onClick={navigateToGoBack}
          >
            <Icon name="chevronLeft" size="M" stroke="accentText" />
            뒤로
          </TextButton>
        </Header.Left>
        {isWriter && (
          <MenuDropdown
            trigger={
              <Header.Right>
                <MenuButton>
                  <Icon name="dots" stroke="accentText" />
                </MenuButton>
              </Header.Right>
            }
            position="bottom-right"
          >
            <li onClick={goEditPage}>게시물 수정</li>
            <li onClick={() => openConfirmAlert(productData.productName)}>
              삭제
            </li>
          </MenuDropdown>
        )}
      </Header>
      <Content>
        <ImgContent>
          <ProductImg src={productData.images[0].imgUrl} />
        </ImgContent>
        <InfoContent>
          <SellerInfo nickname={productData.writer.nickname} />
          <MenuDropdown
            trigger={<States name={productData.status} />}
            position="bottom-left"
            size="S"
          >
            {STATES_OF_PRODUCT.filter(
              (status) => status !== productData.status
            ).map((statusList: ProductStatus) => (
              <li
                key={statusList}
                onClick={() => {
                  changeProductStatus.mutate({
                    productId: productData.id,
                    status: statusList,
                  });
                }}
              >
                {statusList}
              </li>
            ))}
          </MenuDropdown>
          <Title>
            <ProductName>{productData.productName}</ProductName>
            <ProductInfo>
              {productData.category.name} ・{' '}
              {displayTimeAgo(productData.createdAt)}
            </ProductInfo>
          </Title>
          <ProductContent>{productData.content}</ProductContent>

          <ProductStats>
            채팅 {productStat.chattingCount} 관심 {productStat.likeCount} 조회{' '}
            {productStat.viewCount}
          </ProductStats>
        </InfoContent>
      </Content>
      <ActionBar>
        <PostBar
          id={productData.id}
          isLiked={productStat.isLiked}
          price={productData.price}
          isWriter={isWriter}
          handleChatButton={handleChatButton}
        />
      </ActionBar>
    </>
  );
};

const MenuButton = styled.button`
  padding: 8px;
`;

const Content = styled.div`
  margin-top: -56px;
  overflow-y: auto;
  width: 100%;
  height: 100%;
  display: flex;
  gap: 16px;
  flex-direction: column;
`;

const ImgContent = styled.div`
  width: 100%;
  height: 400px;
`;

const ProductImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const InfoContent = styled.div`
  padding: 0px 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const Title = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const ProductName = styled.span`
  width: 100%;
  font: ${({ theme: { font } }) => font.displayStrong20};
  color: ${({ theme: { color } }) => color.neutralTextStrong};
`;

const ProductInfo = styled.p`
  width: 100%;
  font: ${({ theme: { font } }) => font.displayDefault12};
  color: ${({ theme: { color } }) => color.neutralTextWeak};
`;

const ProductContent = styled.p`
  padding: 16px 0px;
  width: 100%;
  font: ${({ theme: { font } }) => font.displayDefault16};
  color: ${({ theme: { color } }) => color.neutralText};
`;

const ProductStats = styled.p`
  width: 100%;
  font: ${({ theme: { font } }) => font.displayDefault12};
  color: ${({ theme: { color } }) => color.neutralTextWeak};
`;