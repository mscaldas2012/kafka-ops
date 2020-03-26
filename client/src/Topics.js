
import React, { Component } from 'react';
import {SERVER_URL} from "./config";
import Spinner from './Spinner'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlusCircle, faTrash} from '@fortawesome/free-solid-svg-icons'


import axios from 'axios';

class Topics extends Component {

    state = {
        newTopic: "",
        topicList: null,
        errorMessage: ""
    };

    componentDidMount() {
        this.refreshTopicList()
    }

    refreshTopicList()  {
        axios.get (`${SERVER_URL}/kafka/topics`)
            .then ( res => this.setState({topicList: res.data}));
    }

    deleteTopic = e => {
        axios.delete(`${SERVER_URL}/kafka/topics/${e}`)
            .then(function (resp) {
                console.log(resp)
            });
        console.log(e + "deleted");
        // this.refreshTopicList()
    };

    createTopic() {
        console.log("creating topic " + this.state.newTopic)
        axios.post(`${SERVER_URL}/kafka/topics/${this.state.newTopic}`)
        // this.refreshTopicList()
    };

    handleChangeEvent = e => {
        e.preventDefault();
        this.setState({newTopic: e.target.value})
    };

    render() {
        return (
            <div>
            <div className="row">
                <span className="align-text-bottom"><input type={"text"} value={this.state.newTopic} onChange={this.handleChangeEvent.bind(this)}/>
                <FontAwesomeIcon icon={faPlusCircle} onClick={(e) => this.createTopic()} /></span>
            </div>
                <div className={"row"} >&nbsp;</div>
            <div>{
                this.state.topicList ==null? <Spinner /> :
                    this.state.topicList.map(p =>
                        <div className="text-left d-flex justify-content-between" key={p} >
                            <span onClick={(e) => this.props.onClick(p)} >{p}</span>
                            <span onClick={(e) => { if (window.confirm('Are you sure you wish to delete this topic?')) this.deleteTopic(p)} }><FontAwesomeIcon icon={faTrash} /></span>
                        </div>
                    )
            }</div>
            </div>
        )
    }
}

export default Topics;