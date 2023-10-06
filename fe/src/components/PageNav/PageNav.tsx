import { styled } from 'styled-components';

type Props = {
  curPage: number;
  numberOfPage: number;
};

export const PageNav: React.FC<Props> = ({ curPage, numberOfPage }) => {
  return (
    <Wrapper>
      <PageInfo>
        {curPage} / {numberOfPage}
      </PageInfo>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  position: absolute;
  bottom: 16px;
  right: 16px;
  padding: 8px 16px;
  display: flex;
  justify-content: center;
  align-content: center;
  width: fit-content;
  height: 32px;
  border-radius: 16px;
  background-color: ${({ theme }) => theme.color.neutralBackgroundBlur};
  user-select: none;
`;

const PageInfo = styled.span`
  font: ${({ theme }) => theme.font.displayDefault12};
  color: ${({ theme }) => theme.color.neutralTextWeak};
`;
