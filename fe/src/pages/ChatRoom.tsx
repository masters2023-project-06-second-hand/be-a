import { ChatMessageProps } from '@api/chat/type';
import { TextButton } from '@components/Button/TextButton';
import { Header } from '@components/Header/Header';
import { Icon } from '@components/Icon/Icon';
import styled from 'styled-components';
import { useEffect, useState } from 'react';
import { ActionBar } from '@components/ActionBar/ActionBar';
import { InputBox } from '@components/Input/InputBox';
import { ChatSendButton } from '@components/Button/SendButton';
import { useInput } from '@hooks/useInput';
import { ChatBubble } from '@components/ChatBubble/ChatBubble';
import { ProductBanner } from '@components/ProductBanner/ProductBanner';
import formatPrice from '@utils/formatPrice';
import { userInfoAtom } from '@atoms/userAtom';
import { useAtom } from 'jotai';
import { usePageNavigator } from '@hooks/usePageNavigator';
import { useParams } from 'react-router-dom';
import * as StompJs from '@stomp/stompjs';
import { Client } from '@stomp/stompjs';
import { MenuDropdown } from '@components/Dropdown/MenuDropdown';
import { useToast } from '@components/Toast/useToast';
import { useChatRoom } from '@api/chat/chat';
import { Loading } from './Loading';
import { ErrorPage } from './ErrorPage';
import { CHAT_SOCKET_URL } from '../envConfig';

export const ChatRoom = () => {
  const token = localStorage.getItem('accessToken');
  const { roomId } = useParams();
  const { navigateToGoBack } = usePageNavigator();
  const [userInfo] = useAtom(userInfoAtom);
  const { value, setValue, onChange } = useInput();
  const { navigateToDetail } = usePageNavigator();
  const [client, changeClient] = useState<Client | null>(null);
  const toast = useToast();

  const { data, isLoading, isError } = useChatRoom(parseInt(roomId!));
  const [chatList, setChatList] = useState<ChatMessageProps[]>(data!.messages);

  const connect = () => {
    try {
      const clientData = new StompJs.Client({
        brokerURL: CHAT_SOCKET_URL,
        connectHeaders: {
          Authorization: `Bearer ${token}`,
          chatRoomId: roomId!,
        },
        debug: function (str) {
          console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      // 구독
      clientData.onConnect = function () {
        clientData.subscribe(`/sub/room/${Number(roomId)}`, callback);
      };

      clientData.activate();
      changeClient(clientData);
    } catch (err) {
      toast.error('채팅을 불러오는 중 오류가 발생했습니다.');
    }
  };

  const disConnect = () => {
    if (client === null) {
      return;
    }
    client.deactivate();
  };

  const callback = (message: StompJs.Message) => {
    if (message.body) {
      const newMessage = JSON.parse(message.body);
      setChatList((chats) => [...chats, newMessage.messages]);
    }
  };

  const sendChat = () => {
    if (value === '') {
      return;
    }

    {
      client &&
        client.publish({
          destination: '/pub/message',
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            chatRoomId: parseInt(roomId!),
            message: value,
            senderId: userInfo!.id,
          }),
        });
    }

    setValue('');
  };

  useEffect(() => {
    connect();
    return () => disConnect();
  }, []);

  if (isLoading) return <Loading />;
  if (isError) return <ErrorPage />;

  return (
    <>
      <Header>
        <Header.Left>
          <TextButton
            size="M"
            textColor="neutralText"
            onClick={navigateToGoBack}
          >
            <Icon name="chevronLeft" size="M" stroke="neutralText" />
            뒤로
          </TextButton>
        </Header.Left>
        <Header.Center>{data.opponentName}</Header.Center>
        <MenuDropdown
          trigger={
            <Header.Right>
              <MoreButton>
                <Icon name="dots" stroke="neutralTextStrong" />
              </MoreButton>
            </Header.Right>
          }
          position="bottom-right"
        >
          <li onClick={() => {}}>알람 끄기</li>
          <li onClick={() => {}}>신고하기</li>
          <li onClick={disConnect}>채팅방 나가기</li>
        </MenuDropdown>
      </Header>
      <Content>
        <ProductBanner
          imgUrl={data.product.thumbnailUrl}
          name={data.product.name}
          price={formatPrice(data.product.price)}
          onClick={() => {
            navigateToDetail(data.product.id);
          }}
        />
        <ChatArea>
          {chatList.map((message) =>
            message.senderId == userInfo?.id ? (
              <ChatBubble message={message.message} />
            ) : (
              <ChatBubble type="opponent" message={message.message} />
            )
          )}
        </ChatArea>
        {chatList.length === 0 && (
          <EmptyList>
            <ChatBubble type="system" message="채팅 내역이 없습니다" />
          </EmptyList>
        )}
      </Content>
      <ActionBar>
        <InputBox value={value} onChange={onChange} />
        <ChatSendButton onClick={sendChat} />
      </ActionBar>
    </>
  );
};

const MoreButton = styled.button`
  padding: 8px;
`;

const Content = styled.div`
  width: 393px;
  padding: 56px 0px 64px 0px;
`;

const EmptyList = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
`;

const ChatArea = styled.div`
  padding: 8px 16px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
`;
