import React from "react";
import "./header-style.css";
import "../../assets/styles/main-style.css";
import Modal from 'react-bootstrap/Modal'
import {Link} from "react-router-dom";

export default class Header extends React.Component{
    constructor(props) {
        super(props);
        this.handleShow = this.handleShow.bind(this);
        this.handleClose = this.handleClose.bind(this);

        this.state = {
            show: false,
            cancel: false,
            email: this.props.value
        };
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
                            <li><Link to= {"/user/watchlist/" }><a className="dropdown-item" href="#">watch list</a></Link></li>
                        </ul>

                    </div>
                    <div className="col-10"/>
                    <div className="col-1 padding-left-0"><img className="imedb-logo" src={require("../../assets/images/logo.png")}
                                                               alt="imedb_logo"/></div>
                </div>
            </div>

        );
    }
}