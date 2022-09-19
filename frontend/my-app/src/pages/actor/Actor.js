
import * as React from "react";
import {toast, ToastContainer} from "react-toastify";
import API from '../../apis/api';
import Header from "../general/Header";
import "../../assets/styles/main-style.css";
import "./actor-syle.css";
import 'react-toastify/dist/ReactToastify.css';
import axios from "axios";
import MovieBox from "../../components/actor/movieBox/movieBox";
import authHeader from '../../services/auth-header.js'


export default class Actor extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            actorInfo: ''
        }
        this.getActorInfoFromAPI = this.getActorInfoFromAPI.bind(this);

    }


    async getActorInfoFromAPI(actorId) {
        API.get('http://87.247.187.217:32666/actors/' + actorId, {headers: authHeader()}).then(resp => {
            if(resp.status == 200) {
                this.setState({actorInfo: resp.data})
            }
            else{
                toast.error('خطا در انجام عملیات')
            }}).catch(error => {
            if(error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }




    componentDidMount() {
        document.title = "Actor";
        this.getActorInfoFromAPI(this.props.match.params.id)
    }



    render() {
        return (
            <>
                <Header value = {this.state.email}/>
                <div className="container display-inline">
                    <div className="row margin-top-1">
                        <div className="col-4">
                            <img className="actor-pic" src={this.state.actorInfo.image} alt="actor-pic"/>
                        </div>
                        <div className="col-8">
                            <p className="text-align-center margin-top-1 ">مشخصات بازیگر</p>
                            <p className="text-align-right">نام: {this.state.actorInfo.name}</p>
                            <p className="text-align-right">تاریخ تولد: {this.state.actorInfo.birthDate}</p>
                            <p className="text-align-right">ملیت: {this.state.actorInfo.nationality} </p>
                            <p className="text-align-right">تعداد فیلم ها: {this.state.actorInfo.movieCount}</p>
                            <p className="text-align-center">فیلم ها</p>
                            <MovieBox actorInfo = {this.state.actorInfo} />
                        </div>
                    </div>
                </div>
            </>
        );
    }


}


