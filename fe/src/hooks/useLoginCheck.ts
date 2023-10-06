import { useEffect } from 'react';
import { useSetAtom } from 'jotai';
import { isLoginAtom } from '@atoms/loginAtom';
import { publicApi } from '@api/index';
import { fetchUserInfo } from '@api/auth/userInfo';
import { userInfoAtom } from '@atoms/userAtom';
import { useTokenStorage } from './useTokenStorage';

export const useLoginCheck = () => {
  const setIsLogin = useSetAtom(isLoginAtom);
  const setUserInfo = useSetAtom(userInfoAtom);
  const { setToken, getToken, removeToken } = useTokenStorage();

  useEffect(() => {
    const refreshToken = getToken('refreshToken');

    if (!refreshToken) {
      return;
    }

    async function fetchAccessToken() {
      try {
        const { data } = await publicApi.post('/oauth2/token', {
          refreshToken,
        });

        if (data && data.accessToken) {
          setToken('accessToken', data.accessToken);
          setToken('refreshToken', data.refreshToken);

          const userInfo = await fetchUserInfo(data.memberId);
          setUserInfo(userInfo);
          setIsLogin(true);
        }
      } catch (error) {
        removeToken('accessToken');
        removeToken('refreshToken');
        return;
      }
    }

    fetchAccessToken();
  }, []);

  return;
};
