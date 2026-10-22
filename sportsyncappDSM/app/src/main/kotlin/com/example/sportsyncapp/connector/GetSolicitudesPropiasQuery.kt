
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


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface GetSolicitudesPropiasQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetSolicitudesPropiasQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val solicitudess: List<SolicitudessItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class SolicitudessItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeportivo: String?,
  
    val fotoDeportiva: String?,
  
    val fechaSolicitud: com.google.firebase.dataconnect.LocalDate,
  
    val deporte: Deporte,
  
    val tipoPerfil: TipoPerfil,
  
    val institucion: Institucion?,
  
    val equipo: Equipo?,
  
    val estado: Estado,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Deporte(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class TipoPerfil(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombrePerfil: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Institucion(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Equipo(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Estado(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetSolicitudesPropias"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetSolicitudesPropiasQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetSolicitudesPropiasQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetSolicitudesPropiasQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetSolicitudesPropiasQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetSolicitudesPropiasQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetSolicitudesPropiasQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

