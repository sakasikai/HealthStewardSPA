export default function () {
  return {
    userName:null,
    userInfo:{
      userId:null,
      userName:null,
      nickName:null,
      age:null,
      gender:null,
    }, 
    isLogin:false,  // 用户登录状态
    onLoginForm:false, // 登录框显示状态
    onSignupForm:false, 
    targetUrl:"/"
  }
}
