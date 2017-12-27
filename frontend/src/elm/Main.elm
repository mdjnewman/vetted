module Main exposing (..)

import Models exposing (Model, initialModel)
import Navigation exposing (Location)
import Routing
import Update exposing (update)
import View exposing (view)
import Msgs exposing (..)
import Material

init : Location -> ( Model, Cmd Msg )
init location =
    let
        currentRoute =
            Routing.parseLocation location
    in
        ( initialModel currentRoute, Material.init MaterialMsg )



subscriptions : Model -> Sub Msg
subscriptions model =
    Material.subscriptions MaterialMsg model



main : Program Never Model Msg
main =
    Navigation.program Msgs.OnLocationChange
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
