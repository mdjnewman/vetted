module Models exposing (..)

import Clients.New exposing (Client, newClient)
import Material

type Route
    = Home
    | ClientsRoute
    | NotFoundRoute


type alias Model =
    { route : Route
    , client : Client
    , mdl : Material.Model
    }

initialModel : Route -> Model
initialModel route =
    { route = route
    , client = newClient
    , mdl = Material.defaultModel
    }
