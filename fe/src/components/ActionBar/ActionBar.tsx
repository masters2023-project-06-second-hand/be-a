import { styled } from 'styled-components';

type Props = {
  children: React.ReactNode;
};

export const ActionBar: React.FC<Props> = ({ children }) => {
  return <Wrapper>{children}</Wrapper>;
};

const Wrapper = styled.div`
  position: fixed;
  bottom: 0;
  padding: 16px;
  display: flex;
  gap: 8px;
  align-items: center;
  width: 393px;
  height: 64px;
  background-color: ${({ theme }) => theme.color.neutralBackgroundWeak};
  font: ${({ theme }) => theme.font.displayDefault16};
  color: ${({ theme }) => theme.color.neutralTextStrong};
  &::before {
    position: absolute;
    top: 0px;
    left: 0px;
    content: '';
    width: 100%;
    height: 1px;
    background-color: ${({ theme }) => theme.color.neutralBorder};
  }
`;
