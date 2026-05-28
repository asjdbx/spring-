/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2025-07-02 00:23:59
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2025-07-09 18:11:51
 * @FilePath: \blong\src\api\auth.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
//导入reqiest.js
import request from '@/utils/request.js'
//提供条用注册接口的函数名
export const userRegisterService = (registerData)=>{
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key]);
    }
    return request.post('/user/register',params);
}
export const LoginService = (LoginerData)=>{
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for(let key in LoginerData){
        params.append(key,LoginerData[key]);
    }
    return request.post('/user/login',params);
} 
// 用户信息查询
export const userInfoService = ()=>{
    return request.get('/user/info');
}
//删除用户
export const deleteUserService = (id)=>{
    return request.delete('/user/delete?id='+id);
}

//修改密码
export const updateUserService = (user,token)=>{
    return request.patch('/user/updatePassword', user, {
  headers: {
    'Authorization': token
  }
});
}

export const updateUserInfoService = (user)=>{
    return request.put('/user/update', user);
}

export const  usrtAvatarService = (userUrl)=>{
    const img= new URLSearchParams();
    img.append('avatar',userUrl);
    return request.patch('/user/updateAvatar', img);
}

export const  userAll=()=>{
    return request.get('/user/findAll');
}

export const  userupdateStatus= (id,status)=>{
    // console.log(id,status)
    return request.patch(`/user/updateStatus?id=${id}&status=${status}`);
}

export const  userupdateRole= (id,role)=>{
    // console.log(id,role)
    return request.patch(`/user/updateRole?id=${id}&role=${role}`);
}


