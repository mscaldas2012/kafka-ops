import React, { Component } from 'react';
import {SERVER_URL} from "./config";
import Spinner from './Spinner'

import axios from 'axios';

class TopicContent extends Component {

    state = {
        topic: '',
        content: [],
        info: {
            startOffset: -1,
            endOffset: -1
        }
    }
    componentDidUpdate(prevProps, prevState, snapshot)  {
        //this.setState({topic: this.props.topic})
        if (this.props.topic !== undefined && (prevProps.topic === undefined || this.props.topic !== prevProps.topic)) {
            console.log("topic prop: " + this.props.topic);
            this.refreshData()
        }
    }

    componentDidMount() {
        this.refreshData()
    }

    refreshData() {
        if (this.props.topic) {
            axios.get(`${SERVER_URL}/kafka/topics/${this.props.topic}`)
                .then(res => this.setState({content: res.data}));
            axios.get(`${SERVER_URL}/kafka/topics/${this.props.topic}/info`)
                .then(res => { this.setState({info: res.data})});
        }
    }

    render() {
       return ( <div>
               <div className=" d-flex justify-content-start" >
                    <div className="m-2 px-2"><h3>{this.props.topic}</h3></div>
                    <div className="border border-primary  m-2 p-1"><label>Start Offset: </label> <span className="badge badge-success  align-middle">{this.state.info.startOffset}</span></div>
                    <div className="border border-primary  m-2 p-1"><label>End Offset: </label> <span className="badge badge-success">{this.state.info.endOffset}</span></div>
                    <div className="border border-primary  m-2 p-1"><label>Partitions:[ </label> <span>{this.state.info.partitions}</span> ]</div>
               </div>
               <table className="table "><thead className="table-dark"><tr><th>Messages</th></tr></thead><tbody>
                           {this.state.content.map(it => <tr key={it}><td className="text-left" key={it} >{it}</td></tr>)}
                           </tbody></table>
                </div>
       )
    }
}

export default TopicContent