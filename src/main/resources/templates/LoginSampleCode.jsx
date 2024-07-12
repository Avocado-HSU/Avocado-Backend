import React from 'react';

const onNaverLogin = () => {
  window.location.href = "http://localhost:80/oauth2/authorization/naver";
}
const onKakaoLogin = () => {
  window.location.href = "http://localhost:80/oauth2/authorization/kakao";
}

const loginPageCheckEndPoind = () => {
  window.location.href = "http://localhost:80/health";
}

const getUserInfo = () => {
  fetch("http://localhost:80/api/user/me", {
    method: "GET",
    credentials: 'include' // 쿠키를 요청에 포함시킵니다
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error('Network response was not ok');
      }
      return res.json();
    })
    .then((data) => {
      console.log(data); // 사용자 정보 처리
      alert(JSON.stringify(data)); // 사용자 정보 확인
    })
    .catch((error) => alert(error));
}

// 데이터 가져오기 함수
const getData = () => {
  fetch("http://localhost:80/yaho", {
    method: "GET",
    credentials: 'include'
  })
    .then((res) => res.json())
    .then((data) => {
      alert(JSON.stringify(data)); // JSON.stringify로 alert 내용 확인
    })
    .catch((error) => alert(error));
}

function Login(props) {
  return (
    <>
      <h1>Login</h1>
      <button onClick={onNaverLogin}>Naver Login</button>
      <button onClick={onKakaoLogin}>Kakao Login</button>
      <button onClick={loginPageCheckEndPoind}>health check</button>
      <button onClick={getUserInfo}>나와라 유저정보</button>

      <button onClick={getData}>데이터 가져오기</button>
    </>
  );
}

// Login 컴포넌트를 내보내기
export default Login;
