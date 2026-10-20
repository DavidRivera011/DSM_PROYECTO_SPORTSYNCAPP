
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


public interface GetEquiposPartidoQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetEquiposPartidoQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val equiposs: List<EquipossItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class EquipossItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEquipo: String,
  
    val institucion: Institucion?,
  
    val deporte: Deporte?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Institucion(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class Deporte(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreDeporte: String,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetEquiposPartido"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetEquiposPartidoQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetEquiposPartidoQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetEquiposPartidoQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetEquiposPartidoQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetEquiposPartidoQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetEquiposPartidoQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

