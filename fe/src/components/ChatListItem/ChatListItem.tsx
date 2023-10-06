import { ChatListProps } from '@api/chat/type';
import { usePageNavigator } from '@hooks/usePageNavigator';
import displayTimeAgo from '@utils/displayTimeAgo';
import styled from 'styled-components';

export const ChatListItem = ({ item }: { item: ChatListProps }) => {
  const { navigateToChatRoom } = usePageNavigator();
  const hasUnreadMessage = item.message.unreadMessageCount !== 0;

  return (
    <Wrapper
      onClick={() => {
        navigateToChatRoom(item.id);
      }}
    >
      <Container>
        <OpponentInfo>
          <OpponentImg src={item.opponent.thumbnailUrl}></OpponentImg>
          <Contents>
            <UserInfo>
              <Nickname>{item.opponent.name}</Nickname>
              <TimeStamp>{displayTimeAgo(item.message.sendAt)}</TimeStamp>
            </UserInfo>
            <MessageInfo>
              <LatestMessage>{item.message.message}</LatestMessage>
            </MessageInfo>
          </Contents>
        </OpponentInfo>
        {hasUnreadMessage && (
          <Counter>{item.message.unreadMessageCount}</Counter>
        )}
        <ProductThumbnail src={item.product.thumbnailUrl}></ProductThumbnail>
      </Container>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  padding: 16px 0px;
  width: 100%;
  height: 80px;
  border-bottom: 0.8px solid ${({ theme: { color } }) => color.neutralBorder};
`;

const Container = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: space-between;
`;

const OpponentInfo = styled.div`
  display: flex;
  height: 100%;
  gap: 8px;
`;

const OpponentImg = styled.img`
  width: 48px;
  height: 48px;
  border-radius: ${({ theme: { radius } }) => radius.half};
  object-fit: cover;
`;

const Contents = styled.div`
  display: flex;
  width: 220px;
  height: 100%;
  flex-direction: column;
  justify-content: space-between;
`;

const UserInfo = styled.div`
  display: flex;
  height: 50%;
  align-items: center;
  gap: 4px;
`;

const Nickname = styled.span`
  font: ${({ theme: { font } }) => font.displayStrong16};
  color: ${({ theme: { color } }) => color.neutralTextStrong};
`;

const TimeStamp = styled.span`
  font: ${({ theme: { font } }) => font.displayDefault12};
  color: ${({ theme: { color } }) => color.neutralTextWeak};
`;

const MessageInfo = styled.div`
  display: flex;
  height: 50%;
  align-items: center;
`;

const LatestMessage = styled.p`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font: ${({ theme: { font } }) => font.displayDefault12};
  color: ${({ theme: { color } }) => color.neutralText};
`;

const Counter = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20px;
  height: 20px;
  color: ${({ theme: { color } }) => color.neutralBackground};
  background-color: ${({ theme: { color } }) => color.accentPrimary};
  border-radius: ${({ theme: { radius } }) => radius.half};
`;

const ProductThumbnail = styled.img`
  width: 48px;
  height: 48px;
  border: ${({ theme: { color } }) => `1px solid ${color.neutralBorder}`};
  border-radius: ${({ theme: { radius } }) => radius.small};
`;
