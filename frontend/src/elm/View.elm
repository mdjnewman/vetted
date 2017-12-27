module View exposing (..)

import Html exposing (Html, div, text)
import Html.Attributes exposing (class, style)
import Models exposing (Model)
import Models exposing (Model)
import Clients.New exposing (view)
import Msgs exposing (..)
import Material.Button as Button
import Material.Toolbar as Toolbar


view : Model -> Html Msg
view model =
    div
      []
      [ Toolbar.render MaterialMsg [0] model.mdl
          [ Toolbar.fixed
          , Toolbar.waterfall 1
          ]
          [ Toolbar.section
              []
              [ Toolbar.row
                  []
                  [ Toolbar.title
                      []
                      [ text "Vetted"
                      ]
                  ]
              ]
          ]
      , Html.div
          [ class "mdc-toolbar-fixed-adjust"
          , style [("padding-top", "10px")]
          ]
          [ page model
          ]
      ]


page : Model -> Html Msg
page model =
    case model.route of
        Models.Home -> text "Home!"

        Models.ClientsRoute ->
            Html.map NewClientPageMessage (Clients.New.view Clients.New.newClient)

        Models.NotFoundRoute ->
            text "Not found"
