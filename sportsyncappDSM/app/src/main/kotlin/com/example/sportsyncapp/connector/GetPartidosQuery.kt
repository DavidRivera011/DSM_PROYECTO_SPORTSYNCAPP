
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


public interface GetPartidosQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetPartidosQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val partidoss: List<PartidossItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class PartidossItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val evento: Evento?,
  
    val deporte: Deporte?,
  
    val equipoLocal: EquipoLocal?,
  
    val equipoVisitante: EquipoVisitante?,
  
    val marcadorLocal: Int?,
  
    val marcadorVisitante: Int?,
  
    val fecha: com.google.firebase.dataconnect.LocalDate?,
  
    val hora: String?,
  
    val estado: Estado?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Evento(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
    val fecha: com.google.firebase.dataconnect.LocalDate,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Deporte(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class EquipoLocal(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class EquipoVisitante(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Estado(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String?,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetPartidos"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetPartidosQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetPartidosQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetPartidosQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetPartidosQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetPartidosQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetPartidosQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

