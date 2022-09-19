import React from "react";
import {toast} from "react-toastify";
import API from '../../../apis/api';




export default class SearchContainer extends React.Component{
    // console.log(props.studentInfo)

    constructor(props) {
        super(props);
        this.state = {
            searchInput : '',
        }
        // this.handlesubmit = this.handlesubmit.bind(this)
        // this.handleSearchKeywordChange = this.handleSearchKeywordChange.bind(this)
    }

    handleSearchKeywordChange(event) {
        this.setState({searchInput:event.target.value });
    }

    handleSubmit(event){
        event.preventDefault();
        this.props.updateMovies(this.props.searchKeyword)
    }

    render() {
        return (
            <>
                <div className="col-7">
                    <div className="search-container">
                        <form className="/action_page.php">
                            {/*onSubmit={this.handleSubmit}*/}
                            <span className="iconify" data-icon="ei:search"></span>
                            <input type="text" name="search" className="search-input margin-top-1" />
                            {/*onChange={this.handleSearchKeywordChange} value={this.props.searchKeyword}*/}
                        </form>
                    </div>
                </div>
                <div className="col-5 padding-0 text-align-right">
                    <button type="button" className="btn btn-search dropdown-toggle padding-0" data-toggle="dropdown">
                        :جستجو بر اساس
                    </button>
                    <div className="dropdown-menu dropdown-style">
                        <a className="dropdown-item" href="#">نام</a>
                        <a className="dropdown-item" href="#">ژانر</a>
                        <a className="dropdown-item" href="#">تاریخ تولید</a>
                    </div>
                </div>
            </>

        );
    }


}

// export default SearchContainer;












