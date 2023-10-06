export const API_ENDPOINTS = {
  LOGIN: (provider: string, queryString: string) =>
    `/login/oauth2/code/${provider}?${queryString}`,
  LOGOUT: '/members/signout',
  SIGNUP: '/members/signup',

  CATEGORIES: (includeImages: boolean) =>
    `/categories?includeImages=${includeImages}`,
  REGIONS: '/regions',
  PRODUCTS: '/products',
  DELETE_PRODUCT: (productId: number) => `/products/${productId}`,
  DELETE_PRODUCT_IMG: (imgId: number) => `/images/${imgId}`,
  POST_NEW_PRODUCT: () => '/products',
  POST_PRODUCT_IMG: () => '/products/images',
  POST_CHAT_ID: '/chats/room-id',
  PUT_PRODUCT: (productId: number) => `/products/${productId}`,
  PRODUCT_DETAIL: (productId: number) => `/products/${productId}`,
  PRODUCT_STATUS: (productId: number) => `/products/${productId}/status`,
  PRODUCT_STAT: (productId: number) => `/products/${productId}/stat`,
  UPDATE_LIKE: (productId: number) => `/products/${productId}/likes`,
  USER_INFO: (memberId: number) => `/members/${memberId}`,
  USER_REGIONS: (memberId: number) => `/members/${memberId}/regions`,
  CHAT_LIST: (memberId: number) => `/members/${memberId}/chats`,
  CHAT_ROOM: (chatRoomId: number) => `/chats/${chatRoomId}`,
};