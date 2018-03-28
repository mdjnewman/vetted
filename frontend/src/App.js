import React, { Component } from 'react'
import logo from './logo.svg'
import './App.css'
import debounce from './debounce.js'


class App extends Component {

  constructor() {
    super()

    this.state = {
      searchTerm: ''
    }

    this.handleChange = this.handleChange.bind(this)

    this.handleChangeDebounced = debounce(function () {
      this.updateData.apply(this, [this.state.searchTerm])
    }, 500)
  }

  updateData(searchTerm) {
    fetch("http://localhost:9001/api/v1/clients/?searchTerm=" + searchTerm)
      .then(r => { if (!r.ok) throw Error(r.statusText); return r })
      .then(r => r.json())
      .then(data => this.setState({ clients: data }))
  }

  componentDidMount() {
    this.updateData(this.state.searchTerm)
  }

  handleChange(event) {
    this.setState({ searchTerm: event.target.value })
    this.handleChangeDebounced()
  }

  render() {
    const loaded = this.state.clients || false

    var clientRows

    if (loaded) {
      clientRows = this.state.clients.map(client => <p>{client.name}</p>)
    } else {
      clientRows = <p>Loading...</p>
    }

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <input value={this.state.searchTerm} onChange={this.handleChange} />
        {clientRows}
      </div>
    )
  }
}

export default App
