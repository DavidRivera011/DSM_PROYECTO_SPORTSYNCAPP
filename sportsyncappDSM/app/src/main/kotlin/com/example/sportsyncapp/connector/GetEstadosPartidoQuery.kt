
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


public interface GetEstadosPartidoQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      DefaultConnector,
      GetEstadosPartidoQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val estadosPartidoss: List<EstadosPartidossItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class EstadosPartidossItem(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombreEstado: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetEstadosPartido"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun GetEstadosPartidoQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    GetEstadosPartidoQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun GetEstadosPartidoQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    GetEstadosPartidoQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun GetEstadosPartidoQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<GetEstadosPartidoQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

