module Models exposing (..)

import Clients.New exposing (Client, newClient)

type Route
    = Home
    | ClientsRoute
    | NotFoundRoute


type alias Model =
    { route : Route
    , client : Client
    }

initialModel : Route -> Model
initialModel route =
    { route = route
    , client = newClient
    }
