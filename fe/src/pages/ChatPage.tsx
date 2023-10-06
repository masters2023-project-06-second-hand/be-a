import { useChatList } from '@api/chat/chat';
import { ChatListItem } from '@components/ChatListItem/ChatListItem';
import { Header } from '@components/Header/Header';
import styled from 'styled-components';
import { Loading } from './Loading';
import { ErrorPage } from './ErrorPage';
import { useAtom } from 'jotai';
import { userInfoAtom } from '@atoms/userAtom';

export const ChatPage = () => {
  const [userInfo] = useAtom(userInfoAtom);
  const { data, isLoading, isError } = useChatList(userInfo!.id);

  if (isLoading) return <Loading />;
  if (isError) return <ErrorPage />;

  return (
    <>
      <Header>
        <Header.Center>채팅</Header.Center>
      </Header>
      <Content>
        {data.length === 0 && <EmptyItem>채팅 내역이 없습니다</EmptyItem>}
        {data.map((item) => (
          <ChatListItem key={item.id} item={item} />
        ))}
      </Content>
    </>
  );
};

const Content = styled.div`
  padding: 56px 16px 56px 16px;
  overflow-y: scroll;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  scrollbar-width: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const EmptyItem = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 80px;
`;
