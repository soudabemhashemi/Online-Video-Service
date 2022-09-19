import React from "react";

import "./movieBox.css"
import {Link} from "react-router-dom";

function MovieBox(props) {
    var items = [];
    for (var i=0; i < props.actorInfo.movieCount; i++) {
        items.push(<div className="col-4 responsive-image">
            <Link to= {"/movies/" + props.actorInfo.acted_in[i].movieId}>
                <img className="movie-pic image" src={props.actorInfo.acted_in[i].image} alt="movie-pic"/>
                <div className="middle">
                    <div className="text">
                        <p>{props.actorInfo.acted_in[i].name}</p>
                        <small>{props.actorInfo.acted_in[i].imdbRate}</small>
                    </div>
                </div>
            </Link>
        </div>);
    }
    return(
        <div className="movies">
            <div className="row">
                {items}
            </div>
        </div>
    );
}

export default MovieBox;

