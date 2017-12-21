module Update exposing (..)

import Models exposing (Model)
import Clients.New
import Msgs exposing (..)
import Routing exposing (parseLocation)

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NewClientPageMessage a ->
            let ( client, cmd ) = Clients.New.update a model.client
            in  ( { model | client = client }, Cmd.map NewClientPageMessage cmd)

        OnLocationChange r ->
          ({ model | route = parseLocation r }, Cmd.none)
