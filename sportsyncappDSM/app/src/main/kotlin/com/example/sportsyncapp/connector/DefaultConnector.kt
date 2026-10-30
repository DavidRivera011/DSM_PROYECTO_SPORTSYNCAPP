
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector

import com.google.firebase.dataconnect.getInstance as _fdcGetInstance
import kotlin.time.Duration.Companion.milliseconds as _milliseconds

public interface DefaultConnector : com.google.firebase.dataconnect.generated.GeneratedConnector<DefaultConnector> {
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect

  
    public val createDeporte: CreateDeporteMutation
  
    public val createEquipo: CreateEquipoMutation
  
    public val createEvento: CreateEventoMutation
  
    public val createInscripcion: CreateInscripcionMutation
  
    public val createInstitucion: CreateInstitucionMutation
  
    public val createPartido: CreatePartidoMutation
  
    public val createSolicitud: CreateSolicitudMutation
  
    public val createUsuario: CreateUsuarioMutation
  
    public val deleteDeporte: DeleteDeporteMutation
  
    public val deleteEquipo: DeleteEquipoMutation
  
    public val deleteEvento: DeleteEventoMutation
  
    public val deleteInscripcion: DeleteInscripcionMutation
  
    public val deleteInstitucion: DeleteInstitucionMutation
  
    public val deletePartido: DeletePartidoMutation
  
    public val deleteSolicitud: DeleteSolicitudMutation
  
    public val getCurrentUsuario: GetCurrentUsuarioQuery
  
    public val getDeportes: GetDeportesQuery
  
    public val getDeportesEvento: GetDeportesEventoQuery
  
    public val getEquipos: GetEquiposQuery
  
    public val getEquiposPartido: GetEquiposPartidoQuery
  
    public val getEstadosEvento: GetEstadosEventoQuery
  
    public val getEstadosInscripcion: GetEstadosInscripcionQuery
  
    public val getEstadosPartido: GetEstadosPartidoQuery
  
    public val getEstadosSolicitud: GetEstadosSolicitudQuery
  
    public val getEventoPorId: GetEventoPorIdQuery
  
    public val getEventos: GetEventosQuery
  
    public val getEventosPartido: GetEventosPartidoQuery
  
    public val getInscripcionesAdmin: GetInscripcionesAdminQuery
  
    public val getInscripcionesPropias: GetInscripcionesPropiasQuery
  
    public val getInstituciones: GetInstitucionesQuery
  
    public val getInstitucionesEvento: GetInstitucionesEventoQuery
  
    public val getPartidoPorId: GetPartidoPorIdQuery
  
    public val getPartidos: GetPartidosQuery
  
    public val getSolicitudesAdmin: GetSolicitudesAdminQuery
  
    public val getSolicitudesPropias: GetSolicitudesPropiasQuery
  
    public val getTiposPerfil: GetTiposPerfilQuery
  
    public val updateDeporte: UpdateDeporteMutation
  
    public val updateEquipo: UpdateEquipoMutation
  
    public val updateEvento: UpdateEventoMutation
  
    public val updateInscripcion: UpdateInscripcionMutation
  
    public val updateInstitucion: UpdateInstitucionMutation
  
    public val updateMarcadorPartido: UpdateMarcadorPartidoMutation
  
    public val updatePartido: UpdatePartidoMutation
  
    public val updateSolicitud: UpdateSolicitudMutation
  
    public val updateUsuario: UpdateUsuarioMutation
  

  public companion object {
    @Suppress("MemberVisibilityCanBePrivate")
    public val config: com.google.firebase.dataconnect.ConnectorConfig = com.google.firebase.dataconnect.ConnectorConfig(
      connector = "default",
      location = "us-east4",
      serviceId = "sportsyncapp-ec689-2-service",
    )

    public fun getInstance(
      dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
    ):DefaultConnector = synchronized(instances) {
      instances.getOrPut(dataConnect) {
        DefaultConnectorImpl(dataConnect)
      }
    }

    private val instances = java.util.WeakHashMap<com.google.firebase.dataconnect.FirebaseDataConnect, DefaultConnectorImpl>()

    
  }
}

public val DefaultConnector.Companion.instance:DefaultConnector
  get() = getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(
    config
  ))

public fun DefaultConnector.Companion.getInstance(
  settings: com.google.firebase.dataconnect.DataConnectSettings = com.google.firebase.dataconnect.DataConnectSettings()
):DefaultConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(config, settings))

public fun DefaultConnector.Companion.getInstance(
  app: com.google.firebase.FirebaseApp,
  settings: com.google.firebase.dataconnect.DataConnectSettings = com.google.firebase.dataconnect.DataConnectSettings()
):DefaultConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(app, config, settings))

private class DefaultConnectorImpl(
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
) : DefaultConnector {
  
    override val createDeporte by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateDeporteMutationImpl(this)
    }
  
    override val createEquipo by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateEquipoMutationImpl(this)
    }
  
    override val createEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateEventoMutationImpl(this)
    }
  
    override val createInscripcion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateInscripcionMutationImpl(this)
    }
  
    override val createInstitucion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateInstitucionMutationImpl(this)
    }
  
    override val createPartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreatePartidoMutationImpl(this)
    }
  
    override val createSolicitud by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateSolicitudMutationImpl(this)
    }
  
    override val createUsuario by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateUsuarioMutationImpl(this)
    }
  
    override val deleteDeporte by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteDeporteMutationImpl(this)
    }
  
    override val deleteEquipo by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteEquipoMutationImpl(this)
    }
  
    override val deleteEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteEventoMutationImpl(this)
    }
  
    override val deleteInscripcion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteInscripcionMutationImpl(this)
    }
  
    override val deleteInstitucion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteInstitucionMutationImpl(this)
    }
  
    override val deletePartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeletePartidoMutationImpl(this)
    }
  
    override val deleteSolicitud by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteSolicitudMutationImpl(this)
    }
  
    override val getCurrentUsuario by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetCurrentUsuarioQueryImpl(this)
    }
  
    override val getDeportes by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetDeportesQueryImpl(this)
    }
  
    override val getDeportesEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetDeportesEventoQueryImpl(this)
    }
  
    override val getEquipos by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEquiposQueryImpl(this)
    }
  
    override val getEquiposPartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEquiposPartidoQueryImpl(this)
    }
  
    override val getEstadosEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEstadosEventoQueryImpl(this)
    }
  
    override val getEstadosInscripcion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEstadosInscripcionQueryImpl(this)
    }
  
    override val getEstadosPartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEstadosPartidoQueryImpl(this)
    }
  
    override val getEstadosSolicitud by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEstadosSolicitudQueryImpl(this)
    }
  
    override val getEventoPorId by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEventoPorIdQueryImpl(this)
    }
  
    override val getEventos by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEventosQueryImpl(this)
    }
  
    override val getEventosPartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetEventosPartidoQueryImpl(this)
    }
  
    override val getInscripcionesAdmin by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetInscripcionesAdminQueryImpl(this)
    }
  
    override val getInscripcionesPropias by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetInscripcionesPropiasQueryImpl(this)
    }
  
    override val getInstituciones by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetInstitucionesQueryImpl(this)
    }
  
    override val getInstitucionesEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetInstitucionesEventoQueryImpl(this)
    }
  
    override val getPartidoPorId by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetPartidoPorIdQueryImpl(this)
    }
  
    override val getPartidos by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetPartidosQueryImpl(this)
    }
  
    override val getSolicitudesAdmin by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetSolicitudesAdminQueryImpl(this)
    }
  
    override val getSolicitudesPropias by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetSolicitudesPropiasQueryImpl(this)
    }
  
    override val getTiposPerfil by lazy(LazyThreadSafetyMode.PUBLICATION) {
      GetTiposPerfilQueryImpl(this)
    }
  
    override val updateDeporte by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateDeporteMutationImpl(this)
    }
  
    override val updateEquipo by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateEquipoMutationImpl(this)
    }
  
    override val updateEvento by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateEventoMutationImpl(this)
    }
  
    override val updateInscripcion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateInscripcionMutationImpl(this)
    }
  
    override val updateInstitucion by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateInstitucionMutationImpl(this)
    }
  
    override val updateMarcadorPartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateMarcadorPartidoMutationImpl(this)
    }
  
    override val updatePartido by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdatePartidoMutationImpl(this)
    }
  
    override val updateSolicitud by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateSolicitudMutationImpl(this)
    }
  
    override val updateUsuario by lazy(LazyThreadSafetyMode.PUBLICATION) {
      UpdateUsuarioMutationImpl(this)
    }
  

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun operations(): List<com.google.firebase.dataconnect.generated.GeneratedOperation<DefaultConnector, *, *>> =
    queries() + mutations()

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun mutations(): List<com.google.firebase.dataconnect.generated.GeneratedMutation<DefaultConnector, *, *>> =
    listOf(
      createDeporte,
        createEquipo,
        createEvento,
        createInscripcion,
        createInstitucion,
        createPartido,
        createSolicitud,
        createUsuario,
        deleteDeporte,
        deleteEquipo,
        deleteEvento,
        deleteInscripcion,
        deleteInstitucion,
        deletePartido,
        deleteSolicitud,
        updateDeporte,
        updateEquipo,
        updateEvento,
        updateInscripcion,
        updateInstitucion,
        updateMarcadorPartido,
        updatePartido,
        updateSolicitud,
        updateUsuario,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun queries(): List<com.google.firebase.dataconnect.generated.GeneratedQuery<DefaultConnector, *, *>> =
    listOf(
      getCurrentUsuario,
        getDeportes,
        getDeportesEvento,
        getEquipos,
        getEquiposPartido,
        getEstadosEvento,
        getEstadosInscripcion,
        getEstadosPartido,
        getEstadosSolicitud,
        getEventoPorId,
        getEventos,
        getEventosPartido,
        getInscripcionesAdmin,
        getInscripcionesPropias,
        getInstituciones,
        getInstitucionesEvento,
        getPartidoPorId,
        getPartidos,
        getSolicitudesAdmin,
        getSolicitudesPropias,
        getTiposPerfil,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect) =
    DefaultConnectorImpl(dataConnect)

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorImpl &&
    other.dataConnect == dataConnect

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "DefaultConnectorImpl",
      dataConnect,
    )

  override fun toString(): String =
    "DefaultConnectorImpl(dataConnect=$dataConnect)"
}



private open class DefaultConnectorGeneratedQueryImpl<Data, Variables>(
  override val connector: DefaultConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedQuery<DefaultConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: DefaultConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    DefaultConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorGeneratedQueryImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "DefaultConnectorGeneratedQueryImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "DefaultConnectorGeneratedQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

private open class DefaultConnectorGeneratedMutationImpl<Data, Variables>(
  override val connector: DefaultConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedMutation<DefaultConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: DefaultConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    DefaultConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is DefaultConnectorGeneratedMutationImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "DefaultConnectorGeneratedMutationImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "DefaultConnectorGeneratedMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}



private class CreateDeporteMutationImpl(
  connector: DefaultConnector
):
  CreateDeporteMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateDeporteMutation.Data,
      CreateDeporteMutation.Variables
  >(
    connector,
    CreateDeporteMutation.Companion.operationName,
    CreateDeporteMutation.Companion.dataDeserializer,
    CreateDeporteMutation.Companion.variablesSerializer,
  )


private class CreateEquipoMutationImpl(
  connector: DefaultConnector
):
  CreateEquipoMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateEquipoMutation.Data,
      CreateEquipoMutation.Variables
  >(
    connector,
    CreateEquipoMutation.Companion.operationName,
    CreateEquipoMutation.Companion.dataDeserializer,
    CreateEquipoMutation.Companion.variablesSerializer,
  )


private class CreateEventoMutationImpl(
  connector: DefaultConnector
):
  CreateEventoMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateEventoMutation.Data,
      CreateEventoMutation.Variables
  >(
    connector,
    CreateEventoMutation.Companion.operationName,
    CreateEventoMutation.Companion.dataDeserializer,
    CreateEventoMutation.Companion.variablesSerializer,
  )


private class CreateInscripcionMutationImpl(
  connector: DefaultConnector
):
  CreateInscripcionMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateInscripcionMutation.Data,
      CreateInscripcionMutation.Variables
  >(
    connector,
    CreateInscripcionMutation.Companion.operationName,
    CreateInscripcionMutation.Companion.dataDeserializer,
    CreateInscripcionMutation.Companion.variablesSerializer,
  )


private class CreateInstitucionMutationImpl(
  connector: DefaultConnector
):
  CreateInstitucionMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateInstitucionMutation.Data,
      CreateInstitucionMutation.Variables
  >(
    connector,
    CreateInstitucionMutation.Companion.operationName,
    CreateInstitucionMutation.Companion.dataDeserializer,
    CreateInstitucionMutation.Companion.variablesSerializer,
  )


private class CreatePartidoMutationImpl(
  connector: DefaultConnector
):
  CreatePartidoMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreatePartidoMutation.Data,
      CreatePartidoMutation.Variables
  >(
    connector,
    CreatePartidoMutation.Companion.operationName,
    CreatePartidoMutation.Companion.dataDeserializer,
    CreatePartidoMutation.Companion.variablesSerializer,
  )


private class CreateSolicitudMutationImpl(
  connector: DefaultConnector
):
  CreateSolicitudMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateSolicitudMutation.Data,
      CreateSolicitudMutation.Variables
  >(
    connector,
    CreateSolicitudMutation.Companion.operationName,
    CreateSolicitudMutation.Companion.dataDeserializer,
    CreateSolicitudMutation.Companion.variablesSerializer,
  )


private class CreateUsuarioMutationImpl(
  connector: DefaultConnector
):
  CreateUsuarioMutation,
  DefaultConnectorGeneratedMutationImpl<
      CreateUsuarioMutation.Data,
      CreateUsuarioMutation.Variables
  >(
    connector,
    CreateUsuarioMutation.Companion.operationName,
    CreateUsuarioMutation.Companion.dataDeserializer,
    CreateUsuarioMutation.Companion.variablesSerializer,
  )


private class DeleteDeporteMutationImpl(
  connector: DefaultConnector
):
  DeleteDeporteMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteDeporteMutation.Data,
      DeleteDeporteMutation.Variables
  >(
    connector,
    DeleteDeporteMutation.Companion.operationName,
    DeleteDeporteMutation.Companion.dataDeserializer,
    DeleteDeporteMutation.Companion.variablesSerializer,
  )


private class DeleteEquipoMutationImpl(
  connector: DefaultConnector
):
  DeleteEquipoMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteEquipoMutation.Data,
      DeleteEquipoMutation.Variables
  >(
    connector,
    DeleteEquipoMutation.Companion.operationName,
    DeleteEquipoMutation.Companion.dataDeserializer,
    DeleteEquipoMutation.Companion.variablesSerializer,
  )


private class DeleteEventoMutationImpl(
  connector: DefaultConnector
):
  DeleteEventoMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteEventoMutation.Data,
      DeleteEventoMutation.Variables
  >(
    connector,
    DeleteEventoMutation.Companion.operationName,
    DeleteEventoMutation.Companion.dataDeserializer,
    DeleteEventoMutation.Companion.variablesSerializer,
  )


private class DeleteInscripcionMutationImpl(
  connector: DefaultConnector
):
  DeleteInscripcionMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteInscripcionMutation.Data,
      DeleteInscripcionMutation.Variables
  >(
    connector,
    DeleteInscripcionMutation.Companion.operationName,
    DeleteInscripcionMutation.Companion.dataDeserializer,
    DeleteInscripcionMutation.Companion.variablesSerializer,
  )


private class DeleteInstitucionMutationImpl(
  connector: DefaultConnector
):
  DeleteInstitucionMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteInstitucionMutation.Data,
      DeleteInstitucionMutation.Variables
  >(
    connector,
    DeleteInstitucionMutation.Companion.operationName,
    DeleteInstitucionMutation.Companion.dataDeserializer,
    DeleteInstitucionMutation.Companion.variablesSerializer,
  )


private class DeletePartidoMutationImpl(
  connector: DefaultConnector
):
  DeletePartidoMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeletePartidoMutation.Data,
      DeletePartidoMutation.Variables
  >(
    connector,
    DeletePartidoMutation.Companion.operationName,
    DeletePartidoMutation.Companion.dataDeserializer,
    DeletePartidoMutation.Companion.variablesSerializer,
  )


private class DeleteSolicitudMutationImpl(
  connector: DefaultConnector
):
  DeleteSolicitudMutation,
  DefaultConnectorGeneratedMutationImpl<
      DeleteSolicitudMutation.Data,
      DeleteSolicitudMutation.Variables
  >(
    connector,
    DeleteSolicitudMutation.Companion.operationName,
    DeleteSolicitudMutation.Companion.dataDeserializer,
    DeleteSolicitudMutation.Companion.variablesSerializer,
  )


private class GetCurrentUsuarioQueryImpl(
  connector: DefaultConnector
):
  GetCurrentUsuarioQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetCurrentUsuarioQuery.Data,
      GetCurrentUsuarioQuery.Variables
  >(
    connector,
    GetCurrentUsuarioQuery.Companion.operationName,
    GetCurrentUsuarioQuery.Companion.dataDeserializer,
    GetCurrentUsuarioQuery.Companion.variablesSerializer,
  )


private class GetDeportesQueryImpl(
  connector: DefaultConnector
):
  GetDeportesQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetDeportesQuery.Data,
      Unit
  >(
    connector,
    GetDeportesQuery.Companion.operationName,
    GetDeportesQuery.Companion.dataDeserializer,
    GetDeportesQuery.Companion.variablesSerializer,
  )


private class GetDeportesEventoQueryImpl(
  connector: DefaultConnector
):
  GetDeportesEventoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetDeportesEventoQuery.Data,
      Unit
  >(
    connector,
    GetDeportesEventoQuery.Companion.operationName,
    GetDeportesEventoQuery.Companion.dataDeserializer,
    GetDeportesEventoQuery.Companion.variablesSerializer,
  )


private class GetEquiposQueryImpl(
  connector: DefaultConnector
):
  GetEquiposQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEquiposQuery.Data,
      Unit
  >(
    connector,
    GetEquiposQuery.Companion.operationName,
    GetEquiposQuery.Companion.dataDeserializer,
    GetEquiposQuery.Companion.variablesSerializer,
  )


private class GetEquiposPartidoQueryImpl(
  connector: DefaultConnector
):
  GetEquiposPartidoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEquiposPartidoQuery.Data,
      Unit
  >(
    connector,
    GetEquiposPartidoQuery.Companion.operationName,
    GetEquiposPartidoQuery.Companion.dataDeserializer,
    GetEquiposPartidoQuery.Companion.variablesSerializer,
  )


private class GetEstadosEventoQueryImpl(
  connector: DefaultConnector
):
  GetEstadosEventoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEstadosEventoQuery.Data,
      Unit
  >(
    connector,
    GetEstadosEventoQuery.Companion.operationName,
    GetEstadosEventoQuery.Companion.dataDeserializer,
    GetEstadosEventoQuery.Companion.variablesSerializer,
  )


private class GetEstadosInscripcionQueryImpl(
  connector: DefaultConnector
):
  GetEstadosInscripcionQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEstadosInscripcionQuery.Data,
      Unit
  >(
    connector,
    GetEstadosInscripcionQuery.Companion.operationName,
    GetEstadosInscripcionQuery.Companion.dataDeserializer,
    GetEstadosInscripcionQuery.Companion.variablesSerializer,
  )


private class GetEstadosPartidoQueryImpl(
  connector: DefaultConnector
):
  GetEstadosPartidoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEstadosPartidoQuery.Data,
      Unit
  >(
    connector,
    GetEstadosPartidoQuery.Companion.operationName,
    GetEstadosPartidoQuery.Companion.dataDeserializer,
    GetEstadosPartidoQuery.Companion.variablesSerializer,
  )


private class GetEstadosSolicitudQueryImpl(
  connector: DefaultConnector
):
  GetEstadosSolicitudQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEstadosSolicitudQuery.Data,
      Unit
  >(
    connector,
    GetEstadosSolicitudQuery.Companion.operationName,
    GetEstadosSolicitudQuery.Companion.dataDeserializer,
    GetEstadosSolicitudQuery.Companion.variablesSerializer,
  )


private class GetEventoPorIdQueryImpl(
  connector: DefaultConnector
):
  GetEventoPorIdQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEventoPorIdQuery.Data,
      GetEventoPorIdQuery.Variables
  >(
    connector,
    GetEventoPorIdQuery.Companion.operationName,
    GetEventoPorIdQuery.Companion.dataDeserializer,
    GetEventoPorIdQuery.Companion.variablesSerializer,
  )


private class GetEventosQueryImpl(
  connector: DefaultConnector
):
  GetEventosQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEventosQuery.Data,
      Unit
  >(
    connector,
    GetEventosQuery.Companion.operationName,
    GetEventosQuery.Companion.dataDeserializer,
    GetEventosQuery.Companion.variablesSerializer,
  )


private class GetEventosPartidoQueryImpl(
  connector: DefaultConnector
):
  GetEventosPartidoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetEventosPartidoQuery.Data,
      Unit
  >(
    connector,
    GetEventosPartidoQuery.Companion.operationName,
    GetEventosPartidoQuery.Companion.dataDeserializer,
    GetEventosPartidoQuery.Companion.variablesSerializer,
  )


private class GetInscripcionesAdminQueryImpl(
  connector: DefaultConnector
):
  GetInscripcionesAdminQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetInscripcionesAdminQuery.Data,
      Unit
  >(
    connector,
    GetInscripcionesAdminQuery.Companion.operationName,
    GetInscripcionesAdminQuery.Companion.dataDeserializer,
    GetInscripcionesAdminQuery.Companion.variablesSerializer,
  )


private class GetInscripcionesPropiasQueryImpl(
  connector: DefaultConnector
):
  GetInscripcionesPropiasQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetInscripcionesPropiasQuery.Data,
      Unit
  >(
    connector,
    GetInscripcionesPropiasQuery.Companion.operationName,
    GetInscripcionesPropiasQuery.Companion.dataDeserializer,
    GetInscripcionesPropiasQuery.Companion.variablesSerializer,
  )


private class GetInstitucionesQueryImpl(
  connector: DefaultConnector
):
  GetInstitucionesQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetInstitucionesQuery.Data,
      Unit
  >(
    connector,
    GetInstitucionesQuery.Companion.operationName,
    GetInstitucionesQuery.Companion.dataDeserializer,
    GetInstitucionesQuery.Companion.variablesSerializer,
  )


private class GetInstitucionesEventoQueryImpl(
  connector: DefaultConnector
):
  GetInstitucionesEventoQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetInstitucionesEventoQuery.Data,
      Unit
  >(
    connector,
    GetInstitucionesEventoQuery.Companion.operationName,
    GetInstitucionesEventoQuery.Companion.dataDeserializer,
    GetInstitucionesEventoQuery.Companion.variablesSerializer,
  )


private class GetPartidoPorIdQueryImpl(
  connector: DefaultConnector
):
  GetPartidoPorIdQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetPartidoPorIdQuery.Data,
      GetPartidoPorIdQuery.Variables
  >(
    connector,
    GetPartidoPorIdQuery.Companion.operationName,
    GetPartidoPorIdQuery.Companion.dataDeserializer,
    GetPartidoPorIdQuery.Companion.variablesSerializer,
  )


private class GetPartidosQueryImpl(
  connector: DefaultConnector
):
  GetPartidosQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetPartidosQuery.Data,
      Unit
  >(
    connector,
    GetPartidosQuery.Companion.operationName,
    GetPartidosQuery.Companion.dataDeserializer,
    GetPartidosQuery.Companion.variablesSerializer,
  )


private class GetSolicitudesAdminQueryImpl(
  connector: DefaultConnector
):
  GetSolicitudesAdminQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetSolicitudesAdminQuery.Data,
      Unit
  >(
    connector,
    GetSolicitudesAdminQuery.Companion.operationName,
    GetSolicitudesAdminQuery.Companion.dataDeserializer,
    GetSolicitudesAdminQuery.Companion.variablesSerializer,
  )


private class GetSolicitudesPropiasQueryImpl(
  connector: DefaultConnector
):
  GetSolicitudesPropiasQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetSolicitudesPropiasQuery.Data,
      Unit
  >(
    connector,
    GetSolicitudesPropiasQuery.Companion.operationName,
    GetSolicitudesPropiasQuery.Companion.dataDeserializer,
    GetSolicitudesPropiasQuery.Companion.variablesSerializer,
  )


private class GetTiposPerfilQueryImpl(
  connector: DefaultConnector
):
  GetTiposPerfilQuery,
  DefaultConnectorGeneratedQueryImpl<
      GetTiposPerfilQuery.Data,
      Unit
  >(
    connector,
    GetTiposPerfilQuery.Companion.operationName,
    GetTiposPerfilQuery.Companion.dataDeserializer,
    GetTiposPerfilQuery.Companion.variablesSerializer,
  )


private class UpdateDeporteMutationImpl(
  connector: DefaultConnector
):
  UpdateDeporteMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateDeporteMutation.Data,
      UpdateDeporteMutation.Variables
  >(
    connector,
    UpdateDeporteMutation.Companion.operationName,
    UpdateDeporteMutation.Companion.dataDeserializer,
    UpdateDeporteMutation.Companion.variablesSerializer,
  )


private class UpdateEquipoMutationImpl(
  connector: DefaultConnector
):
  UpdateEquipoMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateEquipoMutation.Data,
      UpdateEquipoMutation.Variables
  >(
    connector,
    UpdateEquipoMutation.Companion.operationName,
    UpdateEquipoMutation.Companion.dataDeserializer,
    UpdateEquipoMutation.Companion.variablesSerializer,
  )


private class UpdateEventoMutationImpl(
  connector: DefaultConnector
):
  UpdateEventoMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateEventoMutation.Data,
      UpdateEventoMutation.Variables
  >(
    connector,
    UpdateEventoMutation.Companion.operationName,
    UpdateEventoMutation.Companion.dataDeserializer,
    UpdateEventoMutation.Companion.variablesSerializer,
  )


private class UpdateInscripcionMutationImpl(
  connector: DefaultConnector
):
  UpdateInscripcionMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateInscripcionMutation.Data,
      UpdateInscripcionMutation.Variables
  >(
    connector,
    UpdateInscripcionMutation.Companion.operationName,
    UpdateInscripcionMutation.Companion.dataDeserializer,
    UpdateInscripcionMutation.Companion.variablesSerializer,
  )


private class UpdateInstitucionMutationImpl(
  connector: DefaultConnector
):
  UpdateInstitucionMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateInstitucionMutation.Data,
      UpdateInstitucionMutation.Variables
  >(
    connector,
    UpdateInstitucionMutation.Companion.operationName,
    UpdateInstitucionMutation.Companion.dataDeserializer,
    UpdateInstitucionMutation.Companion.variablesSerializer,
  )


private class UpdateMarcadorPartidoMutationImpl(
  connector: DefaultConnector
):
  UpdateMarcadorPartidoMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateMarcadorPartidoMutation.Data,
      UpdateMarcadorPartidoMutation.Variables
  >(
    connector,
    UpdateMarcadorPartidoMutation.Companion.operationName,
    UpdateMarcadorPartidoMutation.Companion.dataDeserializer,
    UpdateMarcadorPartidoMutation.Companion.variablesSerializer,
  )


private class UpdatePartidoMutationImpl(
  connector: DefaultConnector
):
  UpdatePartidoMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdatePartidoMutation.Data,
      UpdatePartidoMutation.Variables
  >(
    connector,
    UpdatePartidoMutation.Companion.operationName,
    UpdatePartidoMutation.Companion.dataDeserializer,
    UpdatePartidoMutation.Companion.variablesSerializer,
  )


private class UpdateSolicitudMutationImpl(
  connector: DefaultConnector
):
  UpdateSolicitudMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateSolicitudMutation.Data,
      UpdateSolicitudMutation.Variables
  >(
    connector,
    UpdateSolicitudMutation.Companion.operationName,
    UpdateSolicitudMutation.Companion.dataDeserializer,
    UpdateSolicitudMutation.Companion.variablesSerializer,
  )


private class UpdateUsuarioMutationImpl(
  connector: DefaultConnector
):
  UpdateUsuarioMutation,
  DefaultConnectorGeneratedMutationImpl<
      UpdateUsuarioMutation.Data,
      UpdateUsuarioMutation.Variables
  >(
    connector,
    UpdateUsuarioMutation.Companion.operationName,
    UpdateUsuarioMutation.Companion.dataDeserializer,
    UpdateUsuarioMutation.Companion.variablesSerializer,
  )


