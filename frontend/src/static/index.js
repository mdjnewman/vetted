'use strict';

require("bootstrap");

require('bootstrap/dist/css/bootstrap.css');

// inject bundled Elm app into div#main
var Elm = require( '../elm/Main' );
Elm.Main.embed( document.getElementById( 'main' ) );
