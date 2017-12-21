module Msgs exposing (..)

import Navigation exposing (Location)
import Clients.New


type Msg
    = NewClientPageMessage Clients.New.Msg
    | OnLocationChange Location
