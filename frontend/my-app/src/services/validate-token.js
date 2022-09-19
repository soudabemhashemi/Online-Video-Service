import jwt_decode from "jwt-decode";

function validateToken() {
    const token = localStorage.getItem("token")
    if(token) {
      const decode = jwt_decode(token)
      const now = Math.floor(Date.now() / 1000)
      if(!decode) {
        localStorage.removeItem("token")
        return false;
      }
      if(decode.exp < now) {
        localStorage.removeItem("token")
        return false;
      }
      else {
        return true;
      }
    }
    else {
        return false;
    }
  }

export default validateToken;