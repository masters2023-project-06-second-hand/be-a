import { styled } from 'styled-components';
import { Icon } from '@components/Icon/Icon';

type Props = {
  onClick(): void;
};

export const ChatSendButton: React.FC<Props> = ({ onClick }) => {
  return (
    <StyledButton onClick={onClick}>
      <Icon name="send" size="S" stroke="accentText" />
    </StyledButton>
  );
};

const StyledButton = styled.button`
  width: 32px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: ${({ theme }) => theme.color.accentPrimary};
  user-select: none;
  cursor: pointer;
`;
