import React, {Component} from 'react';
import './App.css';

import Topics from './Topics.js'
import TopicContent from './TopicContent'


class App extends Component {

  state = {
    selectedTopic: "test1"
  }

  changeTopic = t => {
      this.setState({selectedTopic: t})
      console.log("topic selected - " + t)
  }

  render() {
    return (
        <div className="App">
          <div className=" container-fluid px-4">
            <div className="row App-header text-right">
              <div className="col">Kafka OPS</div>
            </div>

            <div className="row text-center">
              <div className="col-3"><h2>Topics</h2></div>
              <div className="col-9">
                <ul className="nav nav-tabs d-flex justify-content-around " id="tab-content">
                  <li className="active"><h2><a data-toggle="tab" href="#info">Info</a></h2></li>
                  {/*<li><h2><a data-toggle="tab" href="#content">Content</a></h2></li>*/}
                  {/*<li><h2><a data-toggle="tab" href="#stream">Stream</a></h2></li>*/}
                </ul>
              </div>
            </div>

            <div className="row">
              <div className="col-3">
                <Topics onClick={this.changeTopic.bind(this)}/>
              </div>
              <div className="col-9">
                <div className="tab-content">
                  <div id="info" className="tab-pane active">
                    <TopicContent topic={this.state.selectedTopic}/>
                  </div>
                  {/*<div id="content" className="tab-pane fade">*/}
                  {/*  <h3>Content</h3>*/}
                  {/*  <p>Some content in menu 1.</p>*/}
                  {/*  <TopicContent/>*/}
                  {/*</div>*/}
                  {/*<div id="stream" className="tab-pane fade">*/}
                  {/*  <h3>Streaming...</h3>*/}
                  {/*  <p>Some content in menu 2.</p>*/}
                  {/*  /!*<TopicStream />*!/*/}
                  {/*</div>*/}
                </div>
              </div>
            </div>
          </div>
        </div>
    );
  }
}

export default App;
