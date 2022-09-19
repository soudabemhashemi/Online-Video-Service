import * as React from "react";
import {toast} from "react-toastify";
import API from '../../apis/api';
import MoviesHeader from "../../components/movies/header/MoviesHeader";
import "./movies-style.css"
import {Link} from "react-router-dom";
import authHeader from '../../services/auth-header.js'


export default class Movies extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            moviesInfo:'',
            searchKeyword: ''
        }
        this.getMoviesInfoFromAPI = this.getMoviesInfoFromAPI.bind(this)
        this.getsortedMovieByImdb = this.getsortedMovieByImdb.bind(this)
        this.getsortedMovieByDate = this.getsortedMovieByDate.bind(this)

    }



    getsortedMovieByImdb(){
        API.get('http://87.247.187.217:32666/movies/?sort_by=imdb', {headers: authHeader()}).then(resp => {
            if(resp.status == 200) {
                this.setState({moviesInfo: resp.data})
            }
            else{
                toast.error('خطا در انجام عملیات')
            }}).catch(error => {
            if(error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }

    getsortedMovieByDate(){
        API.get('http://87.247.187.217:32666/movies/?sort_by=date', {headers: authHeader()}).then(resp => {
            if(resp.status == 200) {
                this.setState({moviesInfo: resp.data})
            }
            else{
                toast.error('خطا در انجام عملیات')
            }}).catch(error => {
            console.log(error)
            if(error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }



    async getMoviesInfoFromAPI() {
        API.get('http://87.247.187.217:32666/movies', {headers: authHeader()} ).then(resp => {
            if(resp.status == 200) {
                this.setState({moviesInfo: resp.data})
            }
            else{
                toast.error('خطا در انجام عملیات')
            }}).catch(error => {
            console.log(error)
            if(error.response.status == 401 || error.response.status == 403) {
                window.location.href = "http://87.247.187.217:31240/login"
            }
        })
    }

    componentDidMount() {
        document.title = "Movies";
        this.getMoviesInfoFromAPI();
    }



    render() {
        var items = []
        for(var i=0; i<this.state.moviesInfo.length; i++)
            items.push(

                    <a className="col-lg-3 col-md-4 responsive-image"  href="#">
                        <Link to= {"/movies/" + this.state.moviesInfo[i].movieId}>
                            <img src={this.state.moviesInfo[i].image} alt="sample" className="image" />
                            <div className="middle">
                                <div className="text">
                                    <p>{this.state.moviesInfo[i].name}</p>
                                    <small>{this.state.moviesInfo[i].imdbRate}</small>
                                </div>
                            </div>
                        </Link>
                    </a>)


        return (
            <>
                <MoviesHeader value = {this.state.email} />
                <div className="container display-inline">
                    <div className="row margin-top-7">
                        <div className="col-2"></div>

                        <div className="col-8 direction-rtl">
                            <div className="row">
                                {items}
                            </div>
                        </div>

                        <div className="col-2 rating-movie">
                            <p>رتبه بندی بر اساس:</p>
                            <div className="dropdown-style rating-dropdown">
                                <a className="rating-dropdown-item" onClick={this.getsortedMovieByDate} href="#">تاریخ</a>
                                <a className="rating-dropdown-item" onClick={this.getsortedMovieByImdb} href="#">imdb امتیاز </a>
                            </div>
                        </div>
                    </div>
                </div>
            </>

    );
    }


}