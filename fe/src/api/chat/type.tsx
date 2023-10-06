export type ChatListProps = {
  id: number;
  product: {
    id: number;
    thumbnailUrl: string;
  };
  opponent: {
    name: string;
    thumbnailUrl: string;
  };
  message: {
    sendAt: string;
    message: string;
    unreadMessageCount: number;
  };
};

export type ChatIdProps = {
  productId: number;
  sellerId: number;
};

export type ChatMessageProps = {
  message: string;
  senderId: number;
};

export type ChatDetailProps = {
  product: {
    id: number;
    name: string;
    price: number;
    thumbnailUrl: string;
  };
  opponentName: string;
  messages: ChatMessageProps[];
};

export type MessageProps = {
  chatRoomId: number;
  messages: {
    message: string;
    senderId: string;
  };
};
