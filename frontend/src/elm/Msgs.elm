module Msgs exposing (..)

import Navigation exposing (Location)
import Clients.New
import Material


type Msg
    = NewClientPageMessage Clients.New.Msg
    | MaterialMsg (Material.Msg Msg)
    | OpenDrawer
    | CloseDrawer
    | OnLocationChange Location
