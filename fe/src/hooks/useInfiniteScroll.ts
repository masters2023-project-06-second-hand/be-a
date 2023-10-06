import { useRef, useCallback, useEffect } from 'react';

type InfiniteScrollProps = {
  isLoading?: boolean;
  hasNextPage?: boolean;
  fetchNextPage: () => void;
  isFetching?: boolean;
};

/* li 태그에 사용할 수 있는 훅 */
export const useInfiniteScroll = ({
  isLoading,
  hasNextPage,
  fetchNextPage,
  isFetching,
}: InfiniteScrollProps) => {
  const observer = useRef<IntersectionObserver | null>(null);

  const lastElementRef = useCallback(
    (node: HTMLLIElement) => {
      if (observer.current) {
        observer.current.disconnect();
        observer.current = null;
      }

      if (isLoading || isFetching || !hasNextPage) return;

      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage) {
          fetchNextPage();
        }
      });

      if (node) observer.current.observe(node);
    },
    [isLoading, isFetching, hasNextPage, fetchNextPage]
  );

  useEffect(() => {
    return () => {
      if (observer.current) {
        observer.current.disconnect();
      }
    };
  }, []);

  return lastElementRef;
};