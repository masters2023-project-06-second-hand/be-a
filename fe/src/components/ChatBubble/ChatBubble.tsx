import styled from 'styled-components';

type ChatBubbleProps = {
  type?: keyof typeof BUBBLE_STYLE;
  message: string;
};

const BUBBLE_STYLE = {
  me: { color: 'accentText', backgroundColor: 'accentPrimary' },
  opponent: {
    color: 'neutralTextStrong',
    backgroundColor: 'neutralBackgroundBold',
  },
  system: {
    color: 'neutralTextWeak',
    backgroundColor: 'neutralBackgroundBold',
  },
};

export const ChatBubble: React.FC<ChatBubbleProps> = ({
  type = 'me',
  message,
}) => {
  return (
    <Wrapper $type={type}>
      <MessageContent $type={type}>{message}</MessageContent>
    </Wrapper>
  );
};

const Wrapper = styled.div<{ $type: keyof typeof BUBBLE_STYLE }>`
  padding: 8px 16px;
  width: fit-content;
  max-width: 256px;
  height: fit-content;
  background-color: ${({ theme: { color }, $type }) =>
    color[BUBBLE_STYLE[$type].backgroundColor]};
  border-radius: ${({ theme: { radius } }) => radius.large};
`;

const MessageContent = styled.p<{ $type: keyof typeof BUBBLE_STYLE }>`
  width: 100%;
  font: ${({ theme: { font } }) => font.displayDefault16};
  color: ${({ theme: { color }, $type }) => color[BUBBLE_STYLE[$type].color]};
`;
