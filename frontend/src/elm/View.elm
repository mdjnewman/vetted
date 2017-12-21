module View exposing (..)

import Html exposing (Html, div, text)
import Models exposing (Model)
import Models exposing (Model)
import Clients.New exposing (view)
import Msgs exposing (..)

view : Model -> Html Msg
view model =
    div []
        [ page model ]


page : Model -> Html Msg
page model =
    case model.route of
        Models.Home -> text "Home!"

        Models.ClientsRoute ->
            Html.map NewClientPageMessage (Clients.New.view Clients.New.newClient)

        Models.NotFoundRoute ->
            text "Not found"
