module Update exposing (..)

import Models exposing (Model)
import Clients.New
import Msgs exposing (..)
import Routing exposing (parseLocation)
import Material
import Material.Drawer.Temporary as Drawer

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NewClientPageMessage a ->
            let ( client, cmd ) = Clients.New.update a model.client
            in  ( { model | client = client }, Cmd.map NewClientPageMessage cmd)

        OnLocationChange r ->
          ({ model | route = parseLocation r }, Cmd.none)

        OpenDrawer ->
            model ! [ Drawer.emit MaterialMsg [0] Drawer.open ]

        CloseDrawer ->
            model ! [ Drawer.emit MaterialMsg [0] Drawer.close ]

        MaterialMsg msg_ ->
          Material.update MaterialMsg msg_ model
