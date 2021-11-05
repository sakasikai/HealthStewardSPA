/*
export function someAction (context) {
}
*/

import {axiosInstance as axios} from "boot/axios.js";
// import axios from 'axios';?? 为什么default不行 ??

export async function asyncSetUserInfo({commit}, token){
    return new Promise(async (resolve)=>{
        // 更新token
        try{
            localStorage.setItem("jwtToken", token);
        }catch(e){
            console.log(e);
        }
        
        // 设置http请求头
        axios.defaults.headers.common["Authorization"] = "Bearer " + token;
        // 存储userName在前端
                
        // 向后端请求 user 详细信息, 全局存储在前端中
        const res = await axios.get("/api/client/"+localStorage.getItem("userName")+"/get/simpleUserInfo");

        commit("setUserInfo", res.data);
        commit("isLogin");

        return resolve();
    })
}


export async function userLogout ({commit}) {
    try{
        localStorage.removeItem("jwtToken");
    }catch(e){
        console.log(e);
    }
    
    commit("setUserInfo", null);
    commit("isLogout");
}

export async function updateUserInfo({commit}){
    return new Promise(async (resolve)=>{
                      
        // 向后端请求 user 详细信息, 全局存储在前端中
        const res = await axios.get("/api/client/"+localStorage.getItem("userName")+"/get/simpleUserInfo");
        commit("setUserInfo", res.data);

        return resolve();
    })
}