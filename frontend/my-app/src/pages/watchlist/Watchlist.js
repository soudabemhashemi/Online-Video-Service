import * as React from "react";
import {toast, ToastContainer} from "react-toastify";
import API from '../../apis/api';
import Header from "../general/Header";
import "../../assets/styles/main-style.css";
import "./watchlist-style.css";
import 'react-toastify/dist/ReactToastify.css';
import WatchlistInfo from "../../components/watchlist/watchlistInfo/watchlistInfo";
import {Link} from "react-router-dom";
import authHeader from '../../services/auth-header.js'


export default class Watchlist extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            watchlistInfo: '',
            suggestedMovies: ''
        }
        this.getWatchlistInfoFromAPI = this.getWatchlistInfoFromAPI.bind(this);
        this.getSuggestedMovies = this.getSuggestedMovies.bind(this);
        this.deleteFromWatchList = this.deleteFromWatchList.bind(this);

    }


    getWatchlistInfoFromAPI() {
        API.get('http://87.247.187.217:32666/user/watchlist', {headers: authHeader()}).then(resp => {
            if (resp.status === 200) {
                this.setState({watchlistInfo: resp.data})
            } else {
                toast.error('خطا در انجام عملیات')
            }
        }).catch(error => {
            console.log(error)
            if (error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }

    deleteFromWatchList(movieId) {
        API.delete('http://87.247.187.217:32666/movies/' + movieId + '/watchlist', {headers: authHeader()}).then(resp => {
            if (resp.status == 200) {
                console.log("it was deleted from watchlist")
                window.location.href = "http://87.247.187.217:32666/user/watchlist"
            } else {
                toast.error('خطا در انجام عملیات')
            }
        }).catch(error => {
            console.log(error)
            if (error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }

    getSuggestedMovies() {
        API.get('http://87.247.187.217:32666/movies/suggested', {headers: authHeader()}).then(resp => {
            if (resp.status === 200) {
                this.setState({suggestedMovies: resp.data})
            } else {
                toast.error('خطا در انجام عملیات')
            }
        }).catch(error => {
            console.log(error)
            if (error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }


    componentDidMount() {
        document.title = "Watchlist";
        this.getWatchlistInfoFromAPI();
        this.getSuggestedMovies();
    }


    render() {
        var items = [];
        for (var i = 0; i < this.state.watchlistInfo.length; i++) {
            items.push(
                <div className="row watchlist-box text-align-right">
                    <Link to={"/movies/" + this.state.watchlistInfo[i].movieId} className="col-3 padding-0">
                        <img src={this.state.watchlistInfo[i].image} alt="sample" className="image watchlist-image"/>
                    </Link>

                    <div className="col-4">
                        <p className="text-align-left">{this.state.watchlistInfo[i].name}</p>
                        <div className="two-part-text"><p>امتیاز IMDB: </p><p>{this.state.watchlistInfo[i].imdbRate}</p>
                        </div>
                        <p>امتیاز کاربران: {this.state.watchlistInfo[i].rating}</p>
                    </div>

                    <div className="col-5 padding-left-0">
                        <button value={this.state.watchlistInfo[i].movieId}
                                onClick={this.deleteFromWatchList.bind(this, this.state.watchlistInfo[i].movieId)}><span
                            className="iconify trash-icon" data-icon="dashicons:trash"></span></button>
                        <p>کارگردان: {this.state.watchlistInfo[i].director}</p>
                        {/*<p>ژانر: {this.state.watchlistInfo[i].genres}</p>*/}
                        <p>تاریخ انتشار: {this.state.watchlistInfo[i].releaseDate}</p>
                        <p>مدت زمان: {this.state.watchlistInfo[i].duration}</p>
                    </div>
                </div>
            );
        }

        var suggested = [];
        for (var i = 0; i < this.state.suggestedMovies.length; i++) {
            suggested.push(<Link to={"/movies/" + this.state.suggestedMovies[i].movieId}
                                 className="col-4 responsive-image padding-4" href="#">
                    <img src={this.state.suggestedMovies[i].image} alt="sample" className="image"/>
                    <div className="middle">
                        <div className="text">
                            <p>{this.state.suggestedMovies[i].name}</p>
                            <small>{this.state.suggestedMovies[i].imdbRate}</small>
                        </div>
                    </div>
                </Link>
            )
        }

        return (
            <>
                <Header value={this.state.email}/>
                <div className="container display-inline">
                    <div className="row margin-top-7">
                        <div className="col-2"></div>
                        <div className="col-8">{items}</div>
                        <div className="col-2"></div>
                    </div>
                </div>
                <div className="container display-inline">
                    <div className="row margin-top-7">
                        <div className="col-1"></div>

                        <div className="col-10 recommendation-box">
                            <p className="text-align-center padding-top-1">فیلم های پیشنهادی:</p>
                            <div className="row">
                                {suggested}
                            </div>
                        </div>

                        <div className="col-1"></div>
                    </div>
                </div>
            </>
        );
    }


}


