import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, onSubmit, onWithOptions)
import Http
import Json.Decode as Decode
import Json.Encode as Encode
import Uuid
import Random.Pcg
import Maybe
import Bootstrap.Grid as Grid
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Button as Button

main : Program Never Model Msg
main =
  Html.program
    { init = (newModel, Random.Pcg.generate CreateUUIDResult Uuid.uuidGenerator)
    , view = view
    , update = update
    , subscriptions = \_ -> Sub.none
    }


-- MODEL


type alias Model =
  { name : String
  , clientId : Maybe Uuid.Uuid
  , addressLineOne : String
  , addressLineTwo : String
  , town : String
  , state : String
  , postcode : String
  }

newModel : Model
newModel =
  Model "" Nothing "" "" "" "" ""



-- UPDATE


type Msg
    = Name String
    | AddressLineOne String
    | AddressLineTwo String
    | Town String
    | State String
    | Postcode String
    | SubmitPost
    | CreateClientCommandResult (Result Http.Error String)
    | CreateUUIDResult Uuid.Uuid


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
  case msg of
    Name name ->
      ({ model | name = name }, Cmd.none)

    AddressLineOne addressLineOne ->
      ({ model | addressLineOne = addressLineOne }, Cmd.none)

    AddressLineTwo addressLineTwo ->
      ({ model | addressLineTwo = addressLineTwo }, Cmd.none)

    Town town ->
      ({ model | town = town }, Cmd.none)

    State state ->
      ({ model | state = state }, Cmd.none)

    Postcode postcode ->
      ({ model | postcode = postcode }, Cmd.none)

    SubmitPost ->
      (model, createClient model)

    CreateClientCommandResult (Ok _) ->
      (newModel, Cmd.none)

    CreateClientCommandResult (Err _) ->
      (model, Cmd.none)

    CreateUUIDResult uuid ->
      ({ model | clientId = Just (uuid) }, Cmd.none)


-- VIEW


view : Model -> Html Msg
view model =
  Grid.container []
    [ Html.h2 [] [ Html.text "Create client" ]
    , Form.form
        [ onSubmit SubmitPost ]
        [ Form.group []
            [ Form.label [for "addressLineOne"] [ text "Address line one" ]
            , Input.text [ Input.id "addressLineOne", Input.onInput AddressLineOne ]
            ]
        , Form.group []
            [ Form.label [for "addressLineTwo"] [ text "Address line two" ]
            , Input.text [ Input.id "addressLineTwo", Input.onInput AddressLineTwo ]
            ]
        , Form.group []
            [ Form.label [for "town"] [ text "Town" ]
            , Input.text [ Input.id "town", Input.onInput Town ]
            ]
        , Form.group []
            [ Form.label [for "state"] [ text "State" ]
            , Input.text [ Input.id "state", Input.onInput State ]
            ]
        , Form.group []
            [ Form.label [for "postcode"] [ text "Postcode" ]
            , Input.text [ Input.id "postcode", Input.onInput Postcode ]
            ]
        , Button.button [ Button.primary] [ text "Submit" ]
        ]
    ]


createClient : Model -> Cmd Msg
createClient model =
  let
    url =
      "http://localhost:9001/api/v1/clients/_create"

    request =
      Http.post url (Http.jsonBody (encodeRequest model)) decodeClientId
  in
    Http.send CreateClientCommandResult request


decodeClientId : Decode.Decoder String
decodeClientId =
  Decode.at ["data", "image_url"] Decode.string

encodeRequest : Model -> Encode.Value
encodeRequest model =
  Encode.object
    [ ("name", Encode.string model.name)
    , ("clientId", encodeClientId model.clientId)
    , ("address", Encode.object
        [ ("addressLineOne", Encode.string model.addressLineOne)
        , ("addressLineTwo", Encode.string model.addressLineTwo)
        , ("town", Encode.string model.town)
        , ("state", Encode.string model.state)
        , ("postcode", Encode.string model.postcode)
        ]
      )
    ]

encodeClientId : Maybe Uuid.Uuid -> Encode.Value
encodeClientId clientId =
  Maybe.withDefault (Encode.null) (Maybe.map Uuid.encode clientId) -- TODO: shouldn't request with null
