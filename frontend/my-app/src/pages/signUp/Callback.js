import * as React from "react";
import {useLocation} from "react-router-dom";
import {toast} from "react-toastify";
import API from '../../apis/api';



export default function Callback(){
    const search = useLocation().search;
    const code = new URLSearchParams(search).get('code')
    sendCodeToBack(code);
}

function sendCodeToBack(code){
    API.get("/auth/callback?code=" + code)
        .then((resp) => {
        if(resp.status === 200) {
            toast.success('ورود با موفقیت انجام شد.')
            localStorage.setItem("token", resp.data)
            window.location.href = "http://87.247.187.217:31240/"
        }
    }).catch(error => {
        toast.error('ایمیل یا گذرواژه نادرست است')
    })
}