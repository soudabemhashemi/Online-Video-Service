import React from "react";


function WatchlistInfo(props) {
    console.log(props)
    return (

    <div className="row watchlist-box text-align-right">
        <div className="col-3 padding-0">
             <img src="{props.value.image}" alt="sample" className="image watchlist-image"/>
        </div>

        <div className="col-4">
             <p className="text-align-left">The Godfather</p>
             <div className="two-part-text"><p>امتیاز IMDB: </p><p>{props.imdbRate}</p></div>
             <p>امتیاز کاربران: {props.rating}</p>
        </div>

        <div className="col-5 padding-left-0">
             <span className="iconify trash-icon" data-icon="dashicons:trash"></span>
             <p>کارگردان: {props.director}</p>
             <p>ژانر: {props.genres}</p>
             <p>تاریخ انتشار: {props.releaseDate}</p>
             <p>مدت زمان: {props.duration}</p>
        </div>
    </div>
    );
}

export default WatchlistInfo;