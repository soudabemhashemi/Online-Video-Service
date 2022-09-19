import * as React from "react";
import {toast, ToastContainer} from "react-toastify";
import API from '../../apis/api';
import Header from "../general/Header";
import "../../assets/styles/main-style.css";
import "./movie-style.css";
import 'react-toastify/dist/ReactToastify.css';
import ActorInMovieInfo from "../../components/movie/ActorInMovieInfo";
import authHeader from '../../services/auth-header.js'


export default class Movie extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            movieInfo: '',
            movieId: '',
            commentText: '',
            action: ''
        }
        this.handleTextChange = this.handleTextChange.bind(this)
        this.getMovieInfoFromAPI = this.getMovieInfoFromAPI.bind(this);
        this.addToWatchList = this.addToWatchList.bind(this);
        this.addComment = this.addComment.bind(this);
        this.addDislike = this.addDislike.bind(this);
        this.addLike = this.addLike.bind(this);
    }

    addDislike(commentId) {
        API.post('http://87.247.187.217:32666/comments/' + commentId + '/vote', "dislike", {headers: authHeader()}).then(resp => {
            if (resp.status == 200 || resp.status == 202) {
                console.log("dislike")
                window.location.href = "http://87.247.187.217:32666/movies/" + this.props.match.params.id;
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

    addLike(commentId) {
        API.post('http://87.247.187.217:32666/comments/' + commentId + '/vote', "like", {headers: authHeader()}).then(resp => {
            if (resp.status == 200 || resp.status == 202) {
                console.log("like")
                window.location.href = "http://87.247.187.217:32666/movies/" + this.props.match.params.id;
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

    addComment() {
        API.post('http://87.247.187.217:32666/movies/' + this.props.match.params.id + '/comment', this.state.commentText, {headers: authHeader()}
        ).then(resp => {
            if (resp.status == 200 || resp.status == 202) {
                console.log("comment was added")
                window.location.href = "http://87.247.187.217:32666/movies/" + this.props.match.params.id;
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

    addToWatchList() {
        API.get('movies/' + this.props.match.params.id + '/watchlist', {headers: authHeader()}).then(resp => {
            if (resp.status == 200 || resp.status == 202) {
                console.log("it was added to watchlist")
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

    getMovieInfoFromAPI(movieId) {
        API.get('http://87.247.187.217:32666/movies/' + movieId, {headers: authHeader()}).then(resp => {
            if (resp.status == 200) {
                this.setState({movieInfo: resp.data})
                this.setState({movieId: movieId})
            } else {
                toast.error('خطا در انجام عملیات')
            }
        }).catch(error => {
            if (error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }

    handleTextChange(event) {
        this.setState({
            commentText: event.target.value
        });
    }


    componentDidMount() {
        document.title = "Movie";
        this.getMovieInfoFromAPI(this.props.match.params.id)
    }


    render() {

        var items = [];
        if (this.state.movieInfo.comments) {
            for (var i = 0; i < this.state.movieInfo.comments.length; i++) {
                items.push(
                    <div className="comment-box">
                        <p className="color-black text-align-right"> {this.state.movieInfo.comments[i].userName} </p>
                        <hr className="line"/>
                        <div className="row">
                            <div className="col-1">
                                <button className="height-0" value={this.state.movieInfo.comments[i].commentId}
                                        onClick={this.addDislike.bind(this, this.state.movieInfo.comments[i].commentId)}>
                                    <span className="iconify color-red"
                                          data-icon="akar-icons:circle-chevron-down-fill"/>
                                    <p className="color-black">{this.state.movieInfo.comments[i].dislike}</p>
                                </button>
                            </div>
                            <div className="col-1">
                                <button className="height-0" value={this.state.movieInfo.comments[i].commentId}
                                        onClick={this.addLike.bind(this, this.state.movieInfo.comments[i].commentId)}>
                                    <span className="iconify color-green"
                                          data-icon="akar-icons:circle-chevron-up-fill"/>
                                    <p className="color-black">{this.state.movieInfo.comments[i].like}</p>
                                </button>
                            </div>
                            <div className="col-10">
                                <p className="color-black text-align-right">{this.state.movieInfo.comments[i].text}</p>
                            </div>
                        </div>
                    </div>
                );
            }
        } else {
            items.push(<div className="spinner-border text-light margin-50"/>)
        }
        return (
            <>
                <Header value={this.state.email}/>
                <div className="container display-inline">
                    <div className="movie-header">
                        <img className="movie-header" src={this.state.movieInfo.coverImage} alt="movie-header"/>
                        <div className="movie-profile">
                            <div className="row">
                                <div className="col-3 padding-0">
                                    <img className="movie-pic" src={this.state.movieInfo.image} alt="movie-profile"/>
                                    <button className="add_to_watchlist-button" onClick={this.addToWatchList}>افزودن به
                                        لیست
                                    </button>
                                </div>
                                <div className="col-6">
                                    <p className="movie-name"> {this.state.movieInfo.name} </p>
                                    <p className="director text-align-right">کارگردان: {this.state.movieInfo.director}</p>
                                    {/*<p className="writers text-align-right">نویسنده: {this.state.movieInfo.writers}</p>*/}
                                    <p className="duration text-align-right">مدت
                                        زمان: {this.state.movieInfo.duration}</p>
                                    <p className="published-date"> زمان انتشار: {this.state.movieInfo.releaseDate}</p>
                                    <hr className="line"/>
                                    <p className="text-align-right"> {this.state.movieInfo.summary}</p>
                                </div>
                                <div className="col-3 rate-box">
                                    <p className="imdb-rate"> {this.state.movieInfo.imdbRate} </p>
                                    <span className="iconify margin-left-52" data-icon="emojione:star"/>
                                    <p className="emteyaz"> امتیاز کاربران </p>
                                    <p className="rating"> {this.state.movieInfo.rating} </p>
                                    <p className="number-of-rate"> (23 رای) </p>
                                </div>
                            </div>
                            <ActorInMovieInfo actorInfo = {this.state.movieInfo} />

                            <div className="comments-box">
                                <p className="box-title"> دیدگاه ها</p>
                                <div className="register-comment-box">
                                    <input className="comment-input" onChange={this.handleTextChange}
                                           placeholder=":دیدگاه خود را اضافه کنید" name="unname" required=""/>
                                    <hr className="line"/>
                                    <button className="register-button" onClick={this.addComment}>ثبت</button>
                                </div>


                                {items}

                            </div>


                        </div>
                    </div>
                </div>
            </>
        );
    }


}


