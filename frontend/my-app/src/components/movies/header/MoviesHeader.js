import React from "react";
import "./moviesHeader-style.css";
import API from '../../../apis/api';
// import Exit from "./modals/Exit";
import authHeader from '../../../services/auth-header.js'
import SearchContainer from "./SearchContainer";
import "../../../assets/styles/main-style.css"
import {toast} from "react-toastify";

export default class MoviesHeader extends React.Component{
    constructor(props) {
        super(props);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleLogout = this.handleLogout.bind(this);

        this.state = {
            show: false,
            cancel: false,
            email: this.props.value,
            searchKeyword : '',
            // updateMovies : this.props.updateMovies,
            // updateSearchKeyword : this.props.updateSearchKeyword

        };
        // this.updateMovies = this.updateMovies.bind(this);
        // this.updateSearchKeyword = this.updateSearchKeyword.bind(this)
    }

    // updateSearchKeyword(newSearchKeyword) {
    //     // this.props.updateSearchKeyword(),
    //         this.setState({searchKeyword: newSearchKeyword})
    // }

    handleLogout(){
        API.get("auth/logout", {headers: authHeader()}).then((resp) => {
            if(resp.status === 200) {
                toast.success('شما خارج شدید')
                localStorage.removeItem("token");
                window.location.href = "http://87.247.187.217:31240/login"
            }
        }).catch(error => {
            toast.error('شما خارج نشدید')
        })
    }

    handleClose() {
        this.setState({ show: false });
    }

    handleShow() {
        this.setState({ show: true });
    }

    render() {
        return (
            <div className="container-fluid header">
                <div className="row">
                    <div className="col-1">
                        <button type="button" className="btn btn-default padding-0" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span className="iconify account-icon" data-icon="ri:account-circle-fill"/>
                        </button>
                        <ul className="dropdown-menu dropdown-style">
                            <li><a className="dropdown-item" href="#">{this.state.email}</a></li>
                            <li><a className="dropdown-item" href="#">watch list</a></li>
                            <li><a className="dropdown-item" onClick={this.handleLogout}> logout</a></li>
                        </ul>

                    </div>
                    <div className="col-10">
                        <div className="row">
                            <SearchContainer />
                            {/*updateMovies={this.props.updateMovies} searchKeyword={this.state.searchKeyword} updateSearchKeyword={this.props.updateSearchKeyword}*/}
                        </div>
                    </div>
                    <div className="col-1 padding-left-0"><img className="imedb-logo" src={require("../../../assets/images/logo.png")}
                                                               alt="imedb_logo"/></div>
                </div>
            </div>

        );
    }
}