
import * as React from "react";
import {toast, ToastContainer} from "react-toastify";
import API from '../../apis/api';
import Header from "../general/Header";
import "./login-styles.css";
import "../../assets/styles/main-style.css";
import 'react-toastify/dist/ReactToastify.css';
import validateToken from '../../services/validate-token'


export default class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
        }
        this.handleEmailChange = this.handleEmailChange.bind(this)
        this.handlePasswordChange = this.handlePasswordChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
        if(validateToken()) {
            window.location.href = "http://87.247.187.217:31240/"
        }
    }
    handleSubmit(e) {
        e.preventDefault();
        if(!this.state.email){
            console.log('email empty -_-')
            toast.error('فیلد ایمیل باید پر باشد')
            return
        }
        API.post('http://87.247.187.217:32666/auth/login', {
            email: this.state.email,
            password : this.state.password
        }).then((resp) => {
            if(resp.status === 200) {
                toast.success('ورود با موفقیت انجام شد.')
                console.log(resp.data)
                localStorage.setItem("token", resp.data)
                window.location.href = "http://87.247.187.217:31240/"
            }
        }).catch(error => {
            toast.error('ایمیل یا گذرواژه نادرست است')
        })
    }

    handleEmailChange(event) {
        this.setState({
            email: event.target.value
        });
    }
    handlePasswordChange(event) {
        this.setState({
            password: event.target.value
        });
    }

    componentDidMount() {
        document.title = "Log in - Iemdb";
        document.body.classList.add("main-bg");
        toast.configure({rtl: true, className: "text-center", position: "top-right"});
    }

    validateToken() {
            if (typeof this.state.email != "string") return false
            console.log('!isNaN(this.state.email)')
            return !isNaN(this.state.email)
    }


    render() {
        return (
            <>
            <Header value = {this.state.email}/>
            <div className="container display-inline">
                <form method="post" className="login-form" onSubmit={this.handleSubmit}>
                    <div className="login-image">
                        <img src={require("../../assets/images/logo-removebg-preview.jpg")} alt="Avatar" className="avatar" height="100"
                             width="400" />
                            <p className="login-text">ورود</p>
                    </div>

                    <div className="direction-rtl">
                        <label className="color-white">ایمیل</label>
                        <input  type="text" className = "text-align-right" onChange={this.handleEmailChange} placeholder="ایمیل خود را وارد کنید." name="uname" required />

                            <div className="row margin-0">
                                <label className="col-6 text-align-right color-white">2گذرواژه</label>
                                <span className="col-6"><a href="#" className="forget-password">گذرواژه خود را فراموش کردم</a></span>
                            </div>
                            <input type="password" className = "text-align-right" onChange={this.handlePasswordChange} placeholder="گذرواژه خود را وارد کنید." name="psw" required />

                                <button type="submit"  className="login-button">ورود</button>
                                <label className="remember-label float-unset color-white">
                                    <input type="checkbox" checked="checked" name="remember" className="checkbox" />
                                        من را به خاطر بسپار
                                </label>
                    </div>
                    <p className="text-align-right margin-right-3"> برای ثبت نام <a href="signup"> اینجا</a> را کلیک کنید. </p>

                </form>
                <ToastContainer />
            </div>
        </>
        );
    }


}


