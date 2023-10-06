import { styled } from 'styled-components';

type Props = {
  imgUrl: string;
  name: string;
  price: string;
  onClick(): void;
};

export const ProductBanner: React.FC<Props> = ({
  imgUrl,
  name,
  price,
  onClick,
}) => {
  return (
    <Wrapper role="button" onClick={onClick}>
      <ProductImg src={imgUrl} alt="상품 이미지" />
      <ProductInfo>
        <Name>{name}</Name>
        <Price>{price}</Price>
      </ProductInfo>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  position: relative;
  display: flex;
  gap: 8px;
  padding: 16px;
  width: 100%;
  height: 80px;
  background-color: white;
  border-bottom: 0.8px solid ${({ theme: { color } }) => color.neutralBorder};
`;

const ProductImg = styled.img`
  width: 48px;
  height: 48px;
  border-radius: 8px;
  border: 1px solid ${({ theme }) => theme.color.neutralBorder};
`;

const ProductInfo = styled.div`
  display: flex;
  flex-direction: column;
  color: ${({ theme }) => theme.color.neutralTextStrong};
`;

const Name = styled.span`
  font: ${({ theme }) => theme.font.displayDefault16};
`;

const Price = styled.span`
  font: ${({ theme }) => theme.font.displayStrong16};
`;
