interface QueryParams {
  regionId: number;
  categoryId: number | null;
}

export const QUERY_KEYS = {
  CATEGORIES: (includeImages?: boolean) => [`categories`, { includeImages }],
  PRODUCT_STAT: (productId?: number) => ['productStat', { productId }],
  REGIONS: (word: string): [string, { word: string }] => ['regions', { word }],
  PRODUCT_DETAIL: (productId?: number) => ['productData', { productId }],
  USER_REGIONS: (memberId: number | null) => [`userRegions`, memberId],
  PRODUCTS: (
    regionId: number,
    categoryId: number | null
  ): [string, QueryParams] => ['products', { regionId, categoryId }],
  CHAT_LIST: (memberId: number) => ['chatList', memberId],
  CHAT_ROOM: (chatRoomId: number) => ['chatRoom', chatRoomId],
  LIKE_CATEGORY: (memberId: number) => ['likeCategory', memberId],
};