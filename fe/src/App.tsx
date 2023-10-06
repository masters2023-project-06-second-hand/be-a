import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { theme } from '@styles/DesignSystem';
import { ThemeProvider } from 'styled-components';
import { RouterProvider } from 'react-router-dom';
import { Loading } from '@pages/Loading';
import { GlobalModal } from '@components/Modal/GlobalModal';
import { Toast } from '@components/Toast/Toast';
import router from '@routes/router';
import { useLoginCheck } from '@hooks/useLoginCheck';

const queryClient = new QueryClient({
  /* options */
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 0,
    },
  },
});

function App() {
  useLoginCheck();

  return (
    <>
      <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools initialIsOpen={false} />
        <ThemeProvider theme={theme}>
          <RouterProvider router={router} fallbackElement={<Loading />} />
          <Toast />
          <GlobalModal />
        </ThemeProvider>
      </QueryClientProvider>
    </>
  );
}

export default App;
