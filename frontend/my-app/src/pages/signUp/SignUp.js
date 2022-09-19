import * as React from "react";
import {toast, ToastContainer} from "react-toastify";
import API from '../../apis/api';
import "./signUp-style.css"


export default class SignUp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name : '',
            nickname : '',
            birthDate : '',
            email: '',
            password: '',
            rePassword: '',
        }
        this.handleNameChange = this.handleNameChange.bind(this)
        this.handleNickNameChange = this.handleNickNameChange.bind(this)
        this.handleBirthDateChange = this.handleBirthDateChange.bind(this)
        this.handleEmailChange = this.handleEmailChange.bind(this)
        this.handlePasswordChange = this.handlePasswordChange.bind(this)
        this.handleRePasswordChange = this.handleRePasswordChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
        this.handleSignUpWithGit = this.handleSignUpWithGit.bind(this)
    }

    handleSubmit(e) {
        debugger
        e.preventDefault();
                if(!this.state.email){
                    toast.warning('فیلد ایمیل باید پر باشد')
                    return;
                }
                if(!this.state.password){
                    toast.warning('فیلد گذرواژه باید پر باشد')
                    return;
                }
                if(!this.state.name){
                    toast.warning('فیلد نام باید پر باشد')
                    return;
                }
                if(!this.state.nickname){
                    toast.warning('فیلد نام خانوادگی باید پر باشد')
                    return;
                }
                if(!this.state.birthDate){
                    toast.warning('فیلد تاریخ تولد باید پر باشد')
                    return;
                }
                if(this.state.password.length < 8){
                    toast.warning('پسورد باید حداقل 8 رقم باشد')
                    return;
                }
                if(this.state.password !== this.state.rePassword){
                    toast.warning('پسوردها مطابقت ندارند')
                    return;
                }
                API.post('auth/signup', {
                    name: this.state.name,
                    nickname: this.state.nickname,
                    birthDate: this.state.birthDate,
                    email: this.state.email,
                    password: this.state.password
                }).then((resp) => {
                    if(resp.status === 200) {
                        toast.success('ساخت اکانت با موفقیت انجام شد - وارد شوید')
                        localStorage.setItem("token", resp.data)
                        window.location.href = "http://87.247.187.217:32666/"
                    }
                }).catch(error => {
                    toast.error('ساخت اکانت موفقیت آمیز نبود - ایمیل تکراری')
                })
    }

    handleSignUpWithGit(e){
        API.get('https://github.com/login/oauth/authorize?client_id=e4b7b4d360ab731e3f58&scope=user', {
            mode: 'no-cors' // 'cors' by default.
        })
            .then((resp) => {
            if(resp.status === 200) {
                toast.success('ساخت اکانت با موفقیت انجام شد - وارد شوید')
                localStorage.setItem("token", resp.data)
                window.location.href = "http://87.247.187.217:32666/"
            }
        }).catch(error => {

            toast.error('ساخت اکانت موفقیت آمیز نبود')
        })

    }

    handleNameChange(event) {
            this.setState({
                name: event.target.value
            });
        }

        handleNickNameChange(event) {
            this.setState({
                nickname: event.target.value
            });
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

        handleRePasswordChange(event) {
                    this.setState({
                        rePassword: event.target.value
                    });
                }

        handleBirthDateChange(event) {
            this.setState({
                birthDate: event.target.value
            });
        }

    render() {
        return (
            <div className="container display-inline">
                <form method="post" className="login-form" onSubmit={this.handleSubmit}>
                    <div className="login-image">
                        <img src={require("../../assets/images/logo-removebg-preview.jpg")} alt="Avatar" className="avatar" height="100" width="400"/>
                        <p className ="signup-text">ثبت نام</p>
                    </div>

                    <div className="direction-rtl">
                        <label className="color-white">ایمیل</label>
                        <input type="text" placeholder="ایمیل خود را وارد کنید." onChange={this.handleEmailChange} required/>

                        <div className="row margin-0">
                            <label className="col-6 text-align-right color-white">گذرواژه</label>
                            <p className = "color-red"> گذرواژه باید دارای حداقل 8 کاراکتر باشد</p>
                        </div>
                        <input type="password" placeholder="گذرواژه خود را وارد کنید." name="psw" onChange={this.handlePasswordChange} required/>

                        <div className="row margin-0">
                            <label className="col-6 text-align-right color-white">تکرار گذرواژه</label>
                        </div>
                        <input type="password" placeholder="گذرواژه خود را تکرار کنید." name="psw" onChange={this.handleRePasswordChange} required/>

                        <label className="color-white">نام</label>
                        <input type="text" placeholder="نام خود را وارد کنید." onChange={this.handleNameChange} required/>

                        <label className="color-white">نام کاربری</label>
                        <input type="text" placeholder="نام کاربری خود را وارد کنید." onChange={this.handleNickNameChange} required/>

                        <label className="color-white">تاریخ تولد</label>
                        <input type="text" placeholder="تاریخ تولد خود را وارد کنید." onChange={this.handleBirthDateChange} required/>

                        <button type="submit" className="login-button">ایجاد</button>

                    </div>
                    <p className="text-align-right margin-right-3"> درصورتی که قبلا اکانت ساخته اید برای ورود <a href="login"> اینجا</a> را کلیک کنید. </p>
                    <p className="text-align-right margin-right-3"> میتوانید برای ثبت نام از اکانت گیتاب <a href="https://github.com/login/oauth/authorize?client_id=e4b7b4d360ab731e3f58&scope=user"> اینجا</a> را کلیک کنید. </p>
                </form>
                {/*<button className ="add_to_watchlist-button" onClick = {this.handleSignUpWithGit}>میتوانید برای ثبت نام از اکانت گیتاب استفاده کنید</button>*/}
                <ToastContainer />
            </div>
        );
    }


}