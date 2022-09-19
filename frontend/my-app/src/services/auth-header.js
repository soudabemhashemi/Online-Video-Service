import { toast } from "react-toastify";
import jwt_decode from "jwt-decode";


function authHeader() {
	const token = localStorage.getItem("token")
	if(token) {
		const decode = jwt_decode(token)
		const now = Math.floor(Date.now() / 1000)
		if(decode.exp < now) {
			toast.error('ورود شما منقضی شده است و باید  مجددا وارد شوید.')
			localStorage.removeItem("token")
			window.location.href = "http://87.247.187.217:31240/login"
		}
		else {
			return {Authorization: token}
		}
	}
	else {
		toast.error('شما وارد سیستم نشده‌اید باید لاگین کنید.')
		window.location.href = "http://87.247.187.217:31240/login"
	}
}

export default authHeader;