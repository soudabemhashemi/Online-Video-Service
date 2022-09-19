import ReactDOM from "react-dom/client";
import React, { Component }  from 'react';
import {BrowserRouter, Routes, Route, useParams} from "react-router-dom";
import Login from "./pages/login/Login";
import "./assets/styles/main-style.css";
import Actor from "./pages/actor/Actor"
import Movies from "./pages/movies/Movies";
import Watchlist from "./pages/watchlist/Watchlist";
import Movie from "./pages/movie/Movie";
import SignUp from "./pages/signUp/SignUp";
import Callback from "./pages/signUp/Callback";


var cors = require('cors')


const ActorWrapper = (props) => {
    const params = useParams();
    return <Actor {...{...props, match: {params}} } />
}

const MovieWrapper = (props) => {
    const params = useParams();
    return <Movie {...{...props, match: {params}} } />
}

export default function App() {
    document.body.style.backgroundColor = "#292929";
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />}>
                </Route>

                <Route  path="/actors/:id" element={<ActorWrapper />}>
                </Route>

                <Route  path="/" element={<Movies />}>
                </Route>

                <Route  path="/user/watchlist" element={<Watchlist />}>
                </Route>

                <Route  path="/movies/:id" element={<MovieWrapper />}>
                </Route>

                <Route path="/signup" element={<SignUp />}>
                </Route>

                <Route path="/callback" element={<Callback />}>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

