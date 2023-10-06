import { API_ENDPOINTS } from '@constants/endpoints';
import { privateApi } from '..';
import { useMutation, useQuery } from '@tanstack/react-query';
import { QUERY_KEYS } from '@api/queryKey';
import { ChatDetailProps, ChatListProps } from './type';
import { usePageNavigator } from '@hooks/usePageNavigator';
import { useToast } from '@components/Toast/useToast';

const getChatList = async (memberId: number) => {
  const { data } = await privateApi.get(API_ENDPOINTS.CHAT_LIST(memberId));

  return data;
};

export const useChatList = (memberId: number) => {
  return useQuery<ChatListProps[]>(QUERY_KEYS.CHAT_LIST(memberId), () =>
    getChatList(memberId)
  );
};

const getChatRoom = async (chatRoomId: number) => {
  const { data } = await privateApi.get(API_ENDPOINTS.CHAT_ROOM(chatRoomId));

  return data;
};

export const useChatRoom = (chatRoomId: number) => {
  return useQuery<ChatDetailProps>(QUERY_KEYS.CHAT_ROOM(chatRoomId), () =>
    getChatRoom(chatRoomId)
  );
};

const postChatRoom = async ({
  productId,
  sellerId,
}: {
  productId: number;
  sellerId: number;
}) => {
  const request = {
    productId: productId,
    sellerId: sellerId,
  };
  const response = await privateApi.post(API_ENDPOINTS.POST_CHAT_ID, request);

  return response.data;
};

export const usePostChatRoom = () => {
  const { navigateToChatRoom } = usePageNavigator();
  const toast = useToast();
  const { mutate } = useMutation(postChatRoom, {
    onSuccess: (data) => {
      const chatRoomId = data.chatRoomId; // responseBody에서 chatRoomId 추출
      navigateToChatRoom(chatRoomId); // chatRoomId로 이동
    },
    onError: () => {
      toast.error('에러가 발생했습니다. 다시 시도해주세요.');
    },
  });
  return { mutate };
};
