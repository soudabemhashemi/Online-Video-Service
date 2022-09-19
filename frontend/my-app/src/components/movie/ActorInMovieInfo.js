import React from "react";
import "../../assets/styles/main-style.css";
import "./actorInMovieCSS.css"
import {Link} from "react-router-dom";


function ActorInMovieInfo(props) {
    var items = [];
    if (props.actorInfo.actors) {
        for (var i = 0; i < props.actorInfo.actors.length; i++) {
            items.push(<Link to={"/actors/" + props.actorInfo.actors[i].actorId} className="responsive-image">
                <img className="actor-pic image" src={props.actorInfo.actors[i].image} alt="actor-pic"/>
                <div className="middle">
                    <div className="text">
                        <p>{props.actorInfo.actors[i].name}</p>
                        <small>Age: {props.actorInfo.actors[i].birthDate}</small>
                    </div>
                </div>
            </Link>);
        }
    } else {
        items.push(<div className="spinner-border text-light margin-50"/>)
    }

    return (

        <div className="actors-box">
            <p className="box-title"> بازیگران</p>
            <div className="scrollbar-menu">


                {items}
            </div>
        </div>
    );
}

export default ActorInMovieInfo;