import { toast } from "react-toastify";

var jwt = require("jsonwebtoken");

function authToken(token) {
    if(token) {
        const decode = jwt.decode(token)
		const now = Math.floor(Date.now() / 1000)
		if(decode.exp < now) {
			toast.error('این لینک منقضی شده است. دوباره درخواست دهید')
		}
        else {
            return token
        }
    }
    else {
        toast.error('این لینک مشکلی دارد. دوباره درخواست دهید')
        window.location.href = "http://87.247.187.217:31240/login"
    }
}

export default authToken;