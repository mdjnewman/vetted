module View exposing (..)

import Html exposing (Html, div, text)
import Html.Attributes exposing (class, style)
import Models exposing (Model)
import Models exposing (Model)
import Clients.New exposing (view)
import Msgs exposing (..)
import Material.Toolbar as Toolbar
import Material.Drawer.Temporary as Drawer
import Material.Theme as Theme
import Material.List as Lists
import Material.Options as Options exposing (styled, cs, css, when)
import Html.Attributes


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
                  [ Toolbar.icon_
                      [ Toolbar.menu
                      , Options.onClick OpenDrawer
                      , css "cursor" "pointer"
                      ]
                      [ styled Html.i
                          [ cs "material-icons"
                          , css "pointer-events" "none"
                          ]
                          [ text "menu"
                          ]
                      ]
                  , Toolbar.title
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

        , Drawer.render MaterialMsg [0] model.mdl []
          [ Drawer.content []
            [ Lists.listItem
              [ Options.attribute (Html.Attributes.href "#clients")
              , Options.onClick CloseDrawer
              ]
              [ Lists.startDetailIcon "clients" []
              , text "Clients"
              ]
            ]
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
